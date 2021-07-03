package com.weblearnex.app.model;

import com.weblearnex.app.entity.setup.Page;
import com.weblearnex.app.entity.setup.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Component
public class SessionUserBean {
    private User user;
    private Map<String, Page> allowPagesUrl;
    private List<String> userBranchKeyList;
    private Long clientId;
}
