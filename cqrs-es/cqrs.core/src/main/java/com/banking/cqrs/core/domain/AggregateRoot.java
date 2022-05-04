package com.banking.cqrs.core.domain;
import com.banking.cqrs.core.events.BaseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//-----clase para mantener la lista de eventos - mantiene estado de la app
public abstract class AggregateRoot {

    protected String id;
    private int version = -1 ;

    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    public String getId() {
        return id;
    }

    public int getVersion(){
        return version;
    }

    public void setVersion(int version){
        this.version = version;
    }

    public List<BaseEvent> getUncommitedChanges(){
        return this.changes;
    }

    public void markChangesAsCommitted(){
        this.changes.clear();
    }

    //ejecucion del metodo apply y si el evento es nuevo se agrega a la lista de changes
    //metodo para ejecutar nuevo evento o reprocesar uno existente
    protected void applyChange(BaseEvent event, Boolean isNewEvent){
        try{
            //ejecutar metodo basado en el evento
            //encontramos el metodo
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true); // lo habilitamos para que pueda ser invocado
            method.invoke(this, event); // invocamos el metodo

        }catch (NoSuchMethodException e){
            logger.severe("No method found for event " + event.getClass().getName());
        }catch (Exception e){
            logger.severe("Error while applying event " + event.getClass().getName());
        }finally {
            if(isNewEvent){
                this.changes.add(event);
            }
        }
    }

    //cuando quiera ejecutar y agregar un nuevo evento
    public void raiseEvent(BaseEvent event){
        applyChange(event, true);
    }

    //reprocesar un evento existente
    public void replayEvent(Iterable<BaseEvent> events){
            events.forEach(event -> applyChange(event, false));
    }
}
