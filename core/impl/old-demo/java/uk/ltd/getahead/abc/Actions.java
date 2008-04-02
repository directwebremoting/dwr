package uk.ltd.getahead.abc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Actions
{
    /**
     *
     */
    public Map<Integer, Service> getAllServices()
    {
        return services;
    }

    /**
     *
     */
    public String deleteService(Service service)
    {
        Service stored = services.get(service.getServid());
        if (stored == null)
        {
            return "Service " + service.getServiceName() + " has already been deleted.";
        }

        if (stored.getServLastUpdate() != service.getServLastUpdate())
        {
            return "Service " + service.getServiceName() + " has been updated. Delete failed";
        }
        
        services.remove(service.getServid());

        return null;
    }

    /**
     *
     */
    public String writeService(Service service)
    {
        Service stored = services.get(service.getServid());
        if (stored != null)
        {
            if (!stored.getServLastUpdate().equals(service.getServLastUpdate()))
            {
                return "Service " + service.getServiceName() + " has been updated. Delete failed";
            }
        }

        if (service.getServid() == null || service.getServid() == -1)
        {
            service.setServid(++maxServid);
        }

        service.setServLastUpdate("" + System.currentTimeMillis());

        services.put(service.getServid(), service);

        return null;
    }

    private int maxServid = 0;
    private Map<Integer, Service> services = new HashMap<Integer, Service>();

    /**
    *
    */
   public Map<Integer, Reminder> getAllReminders()
   {
       return reminders;
   }

   /**
    *
    */
   public String deleteReminder(Reminder reminder)
   {
       Reminder stored = reminders.get(reminder.getReminderId());
       if (stored == null)
       {
           return "Reminder " + reminder.getEmail() + " has already been deleted.";
       }

       reminders.remove(reminder.getReminderId());

       return null;
   }

   private Map<Integer, Reminder> reminders = new HashMap<Integer, Reminder>();
}
