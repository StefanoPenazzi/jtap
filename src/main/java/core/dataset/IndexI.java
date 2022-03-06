package core.dataset;

import java.util.List;

public interface IndexI<T> extends ModelElementI {
	public List<T> getIndex();
	public String getDescription();
}
