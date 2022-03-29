package projects.CTAP.outputAnalysis;

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
	
	public  LinkTimeFlow(Population population,double timeInterval,LinkTimeFlowDataset linkTimeFlowDataset,Config config) {
		this.timeInterval = timeInterval;
		this.population = population;
		this.linkTimeFlowDataset = linkTimeFlowDataset;
		this.config = config;
		//kkkk
	}
	
	public void run() throws Exception {
		
		//initialize resMap
		//String query = "";
		//List<Record> res= data.external.neo4j.Utils.runQuery(query,AccessMode.READ);
		
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
	
	public void saveResults() {
		
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
				for(int i = 0;i<bestPlan.getLocations().length;i++) {
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
						resMap.putIfAbsent(link, new AtomicIntegerArray(27));
						//TODO avoid negative index
						resMap.get(link).addAndGet(0, 1);
						//resMap.get(link)[(int) Math.floor(bestPlan.getTs()[i]/timeInterval)].incrementAndGet();
					}
				}
			}
			
		}
		
		
	}

}
