package requestsresults;

import model.GameData;

import java.util.Collection;

public record ListGameResult(Collection<GameData> games, String message) {
}
