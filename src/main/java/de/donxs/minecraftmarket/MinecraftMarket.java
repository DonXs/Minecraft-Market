package de.donxs.minecraftmarket;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.donxs.minecraftmarket.callback.Callback;
import de.donxs.minecraftmarket.http.HttpClient;
import de.donxs.minecraftmarket.json.PendingCommand;
import de.donxs.minecraftmarket.json.PendingResponse;
import de.donxs.minecraftmarket.json.PendingResult;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Data;


@Data
public class MinecraftMarket implements Market {

    public static final String BASE_URL = "http://www.minecraftmarket.com/api/1.5/";
    public static final String AUTH_URL = "/auth";
    public static final String PENDING_URL = "/pending";
    public static final String EXECUTED_URL = "/executed/";

    private final Gson gson = new GsonBuilder().create();
    private final Set<Integer> executedCommands = Sets.newConcurrentHashSet();
    private final AtomicBoolean authenticated = new AtomicBoolean();

    private final String minecraftmarketKey;
    private final Callback<Collection<PendingResult>> taskCallback;

    @Override
    public void run() {
        
        HttpClient.get(getPendingUrl(), new Callback<String>() {

            @Override
            public void done(String value, Throwable throwable) {

                PendingResponse response = gson.fromJson(value, PendingResponse.class);
                Preconditions.checkState(response.getStatus().equalsIgnoreCase("ok"), "MinecraftMarket has been occoured an error whilst receiving data");

                for (final PendingResult result : response.getResult()) {

                    result.setCommands(Collections2.filter(result.getCommands(), new Predicate<PendingCommand>() {

                        @Override
                        public boolean apply(PendingCommand command) {

                            return !MinecraftMarket.this.executedCommands.contains(command.getId());

                        }

                    }));

                }

                MinecraftMarket.this.taskCallback.done(Collections2.filter(response.getResult(), new Predicate<PendingResult>() {

                    @Override
                    public boolean apply(PendingResult input) {

                        return input.getCommands().size() > 0;

                    }

                }), null);

                for (final PendingResult user : response.getResult()) {

                    for (final PendingCommand command : user.getCommands()) {

                        HttpClient.get(getExecutedUrl(command.getId()), new Callback<String>() {

                            @Override
                            public void done(String value, Throwable throwable) {

                                MinecraftMarket.this.executedCommands.add(command.getId());

                            }

                        });

                    }

                }

            }

        });

    }

    @Override
    public void authenticate() {

        Preconditions.checkArgument(!authenticated.get(), "Already authenticated with MinecraftMarket");

        HttpClient.get(getAuthUrl(), new Callback<String>() {

            @Override
            public void done(String value, Throwable throwable) {

                MinecraftMarket.this.authenticated.set(true);

            }

        });

    }

    @Override
    public String getAuthUrl() {

        return BASE_URL + minecraftmarketKey + AUTH_URL;

    }

    @Override
    public String getPendingUrl() {

        return BASE_URL + minecraftmarketKey + PENDING_URL;

    }

    @Override
    public String getExecutedUrl(int id) {

        return BASE_URL + minecraftmarketKey + EXECUTED_URL + id;

    }

}
