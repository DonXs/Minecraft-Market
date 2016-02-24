package de.donxs.minecraftmarket.json;

import java.util.Collection;
import lombok.Data;


@Data
public class PendingResponse {
    
    private String status;
    private Collection<PendingResult> result;
    
}
