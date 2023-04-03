package ru.yandex.practicum.filmorate.model.enums;

public enum FriendshipStatus {
    CONFIRMED("Confirmed", true),
    UNCONFIRMED("Unconfirmed", false);
    private final String friendshipStatusName;
    private final Boolean friendship;

    FriendshipStatus(String friendshipStatusName, Boolean friendship) {
        this.friendshipStatusName = friendshipStatusName;
        this.friendship = friendship;
    }

    public String getFriendshipStatusName() {
        return friendshipStatusName;
    }

    public Boolean isFriendship() {
        return friendship;
    }

    public static FriendshipStatus fromString(String str) {
        for (FriendshipStatus fs : FriendshipStatus.values()) {
            if (fs.getFriendshipStatusName().equalsIgnoreCase(str)) return fs;
        }
        return null;
    }
}
