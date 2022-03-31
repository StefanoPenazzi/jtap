package projects.CTAP.outputAnalysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.neo4j.driver.AccessMode;
import org.neo4j.driver.Record;

import config.Config;
import core.dataset.DatasetI;
import projects.CTAP.population.Agent;
import projects.CTAP.population.Plan;
import projects.CTAP.population.Population;


public class LinkTimeFlow {
	
	private final double timeInterval;
	private final Population population;
	private final LinkTimeFlowDataset linkTimeFlowDataset;
	private Map<Long,AtomicIntegerArray> resMap = new ConcurrentHashMap<>();
	private final Config config;
	private final String OUTPUT_FILE = "LinkTimeFlow";
	private final static Integer YEAR_HOURS = 8760;
	
	public  LinkTimeFlow(Population population,double timeInterval,LinkTimeFlowDataset linkTimeFlowDataset,Config config) {
		this.timeInterval = timeInterval;
		this.population = population;
		this.linkTimeFlowDataset = linkTimeFlowDataset;
		this.config = config;
		//kkkk
	}
	
	public void run() throws Exception {
		
		int nThreads = this.config.getGeneralConfig().getThreads();
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		
		//split the agent list
		List<List<Agent>> agentsSL = new ArrayList<>();
		int nAgentsXThread = (int)Math.ceil(((double)population.getAgents().size())/((double)nThreads));
		int start = 0;
		int end = nAgentsXThread ;
		for(int i=0;i<nThreads;i++) {
			if (end >= population.getAgents().size()) {
				end = population.getAgents().size();
				agentsSL.add(population.getAgents().subList(start,end));
				break;
			}
			else {
				agentsSL.add(population.getAgents().subList(start,end));
				start = end;
				end = end+nAgentsXThread;
			}
		}	
		
		for(List<Agent> la: agentsSL) {
			executor.execute(new Task(la,this.timeInterval,this.linkTimeFlowDataset,this.resMap));
		}	
		
		awaitTerminationAfterShutdown(executor);
		
	}
	
	public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
	    threadPool.shutdown();
	    try {
	        if (!threadPool.awaitTermination(60, TimeUnit.DAYS)) {
	            threadPool.shutdownNow();
	        }
	    } catch (InterruptedException ex) {
	        threadPool.shutdownNow();
	        Thread.currentThread().interrupt();
	    }
	}
	
	public void saveCsvBase() throws IOException {
		File file = new File(config.getGeneralConfig().getOutputDirectory()+OUTPUT_FILE+".csv");
		 try {
		        FileWriter writer = new FileWriter(file);
		        for(Map.Entry<Long,AtomicIntegerArray> entry : resMap.entrySet()) {
		        	StringBuffer s = new StringBuffer();
		        	s.append(entry.getKey().toString());
		        	s.append(",");
		        	for(int i = 0; i < entry.getValue().length()-1 ;i++) {
		        		s.append(entry.getValue().get(i));
		        		s.append(",");
		        	}
		        	s.append(entry.getValue().get(entry.getValue().length()-1));
		        	s.append("\n");
		        	writer.write(s.toString());
		        }
		        writer.close();
	    } catch(Exception e) {
	        file.delete();
	    }
	}
	
	public void saveDb() throws Exception {
		saveCsvBase();
		data.external.neo4j.Utils.runQuery("match (n)-[r:CTAPTransportLink]->(m) SET r.flows = null", AccessMode.WRITE);
		data.external.neo4j.Utils.runQuery("USING PERIODIC COMMIT 1000 LOAD CSV FROM \"file:///"+config.getGeneralConfig().getOutputDirectory()+OUTPUT_FILE+".csv"+"\" AS row match (n)-[r]->(m) where ID(r) = toInteger(row[0]) set r.flows = apoc.convert.toIntList(row[1..])", AccessMode.WRITE);
	}
	
	private static final class Task implements Runnable {
		
		private List<Agent> agents;
		private LinkTimeFlowDataset dataset;
		private Map<Long,AtomicIntegerArray> resMap;
		private double timeInterval;

		private Task(List<Agent> agents,double timeInterval,DatasetI dataset,Map<Long,AtomicIntegerArray> resMap ) {
			this.agents = agents;
			this.dataset = (LinkTimeFlowDataset)dataset;
			this.resMap = resMap;
			this.timeInterval = timeInterval;
		}
			

		@Override
		public void run() {
			for(Agent agent: this.agents) {
				boolean homeDs = dataset.getCitiesDsIndex().getIndex().contains(agent.getLocationId());
				Plan bestPlan = agent.getOptimalPlans().stream()
						.max(Comparator.comparingDouble(Plan::getValue)).get();
				for(int i = 0;i<bestPlan.getLocations().length-1;i++) {
					List<Long> links = null;
					if(i%2 == 0) {
						if(homeDs) {
							links = dataset.getDs2DsPathParameter()
                        			.getParameter()[0][bestPlan.getLocations()[i]][bestPlan.getLocations()[i+1]];
						}
						else {
							links = dataset.getOs2DsPathParameter()
                        			.getParameter()[0][bestPlan.getLocations()[i]][bestPlan.getLocations()[i+1]];
						}
					}
					else {
                        if(homeDs) {
                        	links = dataset.getDs2DsPathParameter()
                        			.getParameter()[0][bestPlan.getLocations()[i]][bestPlan.getLocations()[i+1]];
						}
						else {
							links = dataset.getDs2OsPathParameter()
                        			.getParameter()[0][bestPlan.getLocations()[i]][bestPlan.getLocations()[i+1]];
						}
					}
					
					for(Long link: links) {
						//TODO change 27 
						resMap.putIfAbsent(link, new AtomicIntegerArray((int) Math.ceil(YEAR_HOURS/timeInterval)));
						//TODO avoid negative index
						//resMap.get(link).addAndGet(0, 1);
						resMap.get(link).incrementAndGet((int) Math.floor(bestPlan.getTs()[i]/timeInterval));
					}
				}
			}
			
		}
		
		
	}

}
