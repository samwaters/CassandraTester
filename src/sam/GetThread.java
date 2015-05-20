package sam;

import java.util.Random;

public class GetThread extends BaseThread
{
	public GetThread(String threadName, String host, String keyspace)
	{
		super(threadName, host, keyspace);
	}
	
	public void run()
	{
		int operationCount = 0;
		long nextPrintTime = System.currentTimeMillis() + 5000;
		Random r = new Random();
		while(this.canRun)
		{
			String rowKey = this.threadName + "-row-" + r.nextInt(1000000);
			this.session.execute("SELECT * FROM samTest WHERE key='" + rowKey + "'");
			operationCount++;
			if(System.currentTimeMillis() >= nextPrintTime)
			{
				System.out.println(this.threadName + " : " + (operationCount / 5) + " reads/s");
				operationCount = 0;
				nextPrintTime = System.currentTimeMillis() + 5000; 
			}
		}
		this.closeConnections();
	}
}
