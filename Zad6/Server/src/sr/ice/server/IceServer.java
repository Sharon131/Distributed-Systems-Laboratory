package sr.ice.server;
// **********************************************************************
//
// Copyright (c) 2003-2019 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

import com.zeroc.Ice.*;

import java.lang.Exception;

public class IceServer
{
	public void t1(String[] args)
	{
		int status = 0;
		Communicator communicator = null;

		try	{
			communicator = Util.initialize(args);

			ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter2", "tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z");

			// servant locator that locates servant when needed (one servant - one object)
			adapter.addServantLocator(new MyServantLocator(), "calc1");

			// default servant object ->  (one servant -> many objects)
			CalcI defaultCalServant = new CalcI("defaultservant1");
			adapter.addDefaultServant(defaultCalServant, "calc2");
	        
			adapter.activate();
			
			System.out.println("Entering event processing loop...");
			
			communicator.waitForShutdown(); 		
			
		}
		catch (Exception e) {
			System.err.println(e);
			status = 1;
		}
		if (communicator != null) {
			try {
				communicator.destroy();
			}
			catch (Exception e) {
				System.err.println(e);
				status = 1;
			}
		}
		System.exit(status);
	}


	public static void main(String[] args)
	{
		IceServer app = new IceServer();
		app.t1(args);
	}
}
