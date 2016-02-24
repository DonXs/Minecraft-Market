package de.donxs.minecraftmarket;


public interface Market extends Runnable {

    /**
     * Runs the {@link Market} scheduled as a task.
     * 
     */
    @Override
    public void run();
    
    /**
     * Authenticates the {@link Market} on the remote server.
     * 
     */
    public void authenticate();
   
    /**
     * Gets the url for authentication as {@link String}.
     * 
     * @return the authentication url
     */
    public String getAuthUrl();

    /**
     * Gets the url for pending commands as {@link String}.
     * 
     * @return the url for pending commands
     */
    public String getPendingUrl();

    /**
     * Gets the url for executed commands with an id as {@link String}.
     * 
     * @param id 
     *        the id from the executed commands.
     * 
     * @return the url for executed commands prepared with the id
     */
    public String getExecutedUrl(int id);
    
    
}
