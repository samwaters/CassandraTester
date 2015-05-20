package sam;

import java.util.Random;

/**
 * Worker thread to perform write operations on the cluster
 * Uses BaseThread for connection management
 * @author Sam Waters <sam@samwaters.com>
 * @date 20-05-2015
 * @see BaseThread for connection open and close logic
 */
public class PutThread extends BaseThread
{
	/**
	 * Set up the cluster
	 * @param threadName Unique name for this thread
	 * @param host The address (FQDN or IP) of the cluster to connect to
	 * @param keyspace The keyspace to use
	 */
	public PutThread(String threadName, String host, String keyspace)
	{
		//All connection logic is done in BaseThread
		super(threadName, host, keyspace);
	}
	
	public void run()
	{
		int operationCount = 0;
		//Print status every 5 seconds
		long nextPrintTime = System.currentTimeMillis() + 5000;
		Random r = new Random();
		while(this.canRun)
		{
			//Row key is chosen at random
			String rowKey = this.threadName + "-row-" + r.nextInt(1000000);
			//Perform a write operation
			this.session.execute("INSERT INTO samTest (key, value1, value2, value3) VALUES('" + rowKey + "', 'This is Value 1', 'This is Value 2', 'This is Value 3')");
			operationCount++;
			if(System.currentTimeMillis() >= nextPrintTime)
			{
				//Print status
				System.out.println(this.threadName + " : " + (operationCount / 5) + " writes/s");
				operationCount = 0;
				nextPrintTime = System.currentTimeMillis() + 5000; 
			}
		}
		//Done, shutdown cleanly
		this.closeConnections();
	}
}
