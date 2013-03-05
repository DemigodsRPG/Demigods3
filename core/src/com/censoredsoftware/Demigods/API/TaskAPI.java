package com.censoredsoftware.Demigods.API;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Handlers.Abstract.DemigodsPlugin;
import com.censoredsoftware.Demigods.Libraries.Objects.PlayerCharacter;
import com.censoredsoftware.Demigods.Libraries.Objects.Task;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskAPI
{
    private static final Demigods API = Demigods.INSTANCE;

    /*
     * invokeTask() : Invokes a task.
     */
    public void invokeTask(DemigodsPlugin plugin, String classPath) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        // No parameters for the invoked method
        Class noparams[] = {};

        // Creates a new instance of the Task class
        Object obj = Class.forName(classPath, true, API.plugin.getClassLoader(plugin)).newInstance();

        // Invoke the 'onInvoke' method.
        Class.forName(classPath, true, API.plugin.getClassLoader(plugin)).getMethod("onInvoke", noparams).invoke(obj, (Object[]) null);
    }
    public void invokeTask(Task task) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        invokeTask(task.getPlugin(), task.getClassPath());
    }

    /*
     * taskExists() : Returns true if a task of this type already exists.
     */
    public boolean taskExists(String quest, int order, PlayerCharacter character)
    {
        for(Map.Entry<Integer, HashMap<String, Object>> entry : API.data.getAllTasks().entrySet())
        {
            for(Map.Entry<String, Object> _entry : entry.getValue().entrySet())
            {
                if(!(_entry.getValue() instanceof Task)) continue;

                Task foundTask = (Task) _entry.getValue();
                if(foundTask.getCharacter() != character) continue;
                if(!foundTask.getQuest().equalsIgnoreCase(quest)) continue;

                if(foundTask.getOrder() == order) return true;
            }
        }
        return false;
    }

    public void invokeAllTasks() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        for(Map.Entry<Integer, HashMap<String, Object>> entry : API.data.getAllTasks().entrySet())
        {
            for(Map.Entry<String, Object> _entry : entry.getValue().entrySet())
            {
                if(!(_entry.getValue() instanceof Task)) continue;

                Task foundTask = (Task) _entry.getValue();
                if(!foundTask.isInstalled()) continue;
                if(!foundTask.isActive()) continue;

                invokeTask(foundTask);
            }
        }
    }

    public ArrayList<Task> getTasks(PlayerCharacter character)
    {
        ArrayList<Task> tasks = new ArrayList<Task>();
        for(Map.Entry<Integer, HashMap<String, Object>> entry : API.data.getAllTasks().entrySet())
        {
            for(Map.Entry<String, Object> _entry : entry.getValue().entrySet())
            {
                if(!(_entry.getValue() instanceof Task)) continue;

                Task foundTask = (Task) _entry.getValue();
                if(foundTask.getCharacter() != character) continue;

                tasks.add(foundTask);
            }
        }

        return tasks;
    }
}
