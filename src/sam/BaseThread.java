package sam;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;

/**
 * Base for Get and Put threads, containing connect and disconnect logic
 * @author Sam Waters <sam@samwaters.com>
 * @date 20-05-2015
 */
public class BaseThread extends Thread
{
	protected String threadName;
	protected Cluster cluster;
	protected Session session;
	protected boolean canRun = true;
	
	/**
	 * Generic constructor to allow extending
	 */
	public BaseThread()
	{
		
	}
	
	/**
	 * 
	 * @param threadName The unique name of this thread
	 * @param host The address (FQDN or IP) of the cluster to connect to
	 * @param keyspace The keyspace to use
	 */
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
	
	/**
	 * Cleanly disconnect from the cluster
	 */
	protected void closeConnections()
	{
		this.cluster.close();
		this.session.close();
	}
	
	/**
	 * Tell this thread to stop operations cleanly when possible
	 * @param forceClose Close all connections now
	 */
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
