

package es.altia.technical;

/**
 * An interace used to define from which entities, you have access to their state.
 * This interface its escecially usefull when using remote object (as EJBs), becouse
 * allous us to set & retrieve the state with only a single remote call. Obviously the 
 * state of the object must be encapsulated in a value object which implements
 * <code>ValueObject</code>.
 */
public interface ApproachableState {
    /**
     * Gets the object state with only a single method call.
     * @return A bean which implements <code>ValueObject</code>.
     */	
    ValueObject getValue();

    /**
     * Sets the object state with only a single method call.
     * @param theState A bean which implements <code>ValueObject</code> and has the 
     * new object state.
     */
    void setValue(ValueObject theState);

    /** @link dependency */
    /*#ValueObject lnkValueObject;*/
}
