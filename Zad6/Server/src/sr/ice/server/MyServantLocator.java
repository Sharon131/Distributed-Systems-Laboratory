package sr.ice.server;

import com.zeroc.Ice.Current;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ServantLocator;
import com.zeroc.Ice.UserException;

public class MyServantLocator implements com.zeroc.Ice.ServantLocator {

    private int counter = 0;

    @Override
    synchronized public LocateResult locate(Current current) throws UserException {
        ServantLocator.LocateResult r = new ServantLocator.LocateResult();

        com.zeroc.Ice.Object servant = current.adapter.find(current.id);
        if(servant == null)
        {
            System.out.println(current.id.category + "/" + current.id.name + ": creating servant " + "locatorservant" + counter);
            servant = new CalcI1("locatorservant" + counter);
            counter++;
            current.adapter.add(servant, current.id);
        }

        r.returnValue = servant;
        return r;
    }

    @Override
    synchronized public void finished(Current current, Object object, java.lang.Object o) throws UserException {
        System.out.println(current.id.category + "/" + current.id.name + ": removing servant after op: " + current.operation);
        current.adapter.remove(current.id);

    }

    @Override
    synchronized public void deactivate(String s) {
        System.out.println("Deactivation for " + s);
    }
}
