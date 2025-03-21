package dev.fastball.platform.web.feature.message;

import dev.fastball.core.component.DataResult;
import dev.fastball.platform.entity.User;

public interface WebPortalMessageAccessor {
    boolean hasUnreadMessage(User currentUser);

    DataResult<Message> loadMessages(User currentUser, Long currentPage);

    boolean readMessage(User currentUser, String messageId);
}
