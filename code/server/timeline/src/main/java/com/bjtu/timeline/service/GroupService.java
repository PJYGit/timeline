package com.bjtu.timeline.service;

import com.bjtu.timeline.bean.response.GroupResponses.*;
import com.bjtu.timeline.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bjtu.timeline.bean.response.CommonResponses.STATE_COMMON_FAIL;
import static com.bjtu.timeline.bean.response.CommonResponses.STATE_COMMON_OK;

@Service
public class GroupService {

    @Autowired
    private GroupMapper groupDao;

    public ListResponse getGroupList(int uid) {
        List<ListResponse.elm> listManage = groupDao.selectListManage(uid);
        List<ListResponse.elm> listIn = groupDao.selectListIn(uid);
        return new ListResponse(STATE_COMMON_OK, listManage, listIn);
    }

    public MakeResponse makeGroup(int uid, String name) {
        int gid = groupDao.makeGroup(uid, name);
        groupDao.addIntoGroup(gid, uid);
        return new MakeResponse(STATE_COMMON_OK);
    }

    public DelResponse deleteGroup(int uid, int gid) {
        int effectCnt = groupDao.deleteGroup(uid, gid);
        if (effectCnt == 0) {
            return new DelResponse(-10);
        }
        groupDao.cleanMember(gid);
        groupDao.cleanApplication(gid);
        return new DelResponse(STATE_COMMON_OK);
    }

}
