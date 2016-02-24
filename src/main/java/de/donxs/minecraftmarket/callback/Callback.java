package de.donxs.minecraftmarket.callback;

public interface Callback<V> {
    
    public void done(V value, Throwable throwable);
    
}
