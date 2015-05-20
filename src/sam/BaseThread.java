package sam;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;

public class BaseThread extends Thread
{
	protected String threadName;
	protected Cluster cluster;
	protected Session session;
	protected boolean canRun = true;
	
	public BaseThread()
	{
		
	}
	
	public BaseThread(String threadName, String host, String keyspace)
	{
		this.threadName = threadName;
		Builder builder = Cluster.builder();
		builder.addContactPoint(host);
		this.cluster = builder.build();
		OperationTimer cTimer = new OperationTimer();
		cTimer.startTiming();
		this.session = this.cluster.connect(keyspace);
		cTimer.stopTiming();
		System.out.println(threadName + " connected in " + cTimer.getTotalTime() + "ms");
	}
	
	protected void closeConnections()
	{
		this.cluster.close();
		this.session.close();
	}
	
	public void shutdown(boolean forceClose)
	{
		System.out.println(this.threadName + " shutting down");
		this.canRun = false;
		if(forceClose)
		{
			this.closeConnections();
		}
	}
}
