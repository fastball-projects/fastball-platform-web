package dev.fastball.platform.web.service.support;

import dev.fastball.core.component.DataResult;
import dev.fastball.platform.entity.User;
import dev.fastball.platform.web.feature.message.Message;
import dev.fastball.platform.web.feature.message.WebPortalMessageAccessor;

public class DefaultWebPortalMessageAccessor implements WebPortalMessageAccessor {
    @Override
    public boolean hasUnreadMessage(User currentUser) {
        return false;
    }

    @Override
    public DataResult<Message> loadMessages(User currentUser, Long currentPage) {
        return DataResult.empty();
    }

    @Override
    public boolean readMessage(User currentUser, String messageId) {
        return false;
    }
}
