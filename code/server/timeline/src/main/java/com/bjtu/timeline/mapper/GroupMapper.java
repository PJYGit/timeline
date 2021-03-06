package com.bjtu.timeline.mapper;

import com.bjtu.timeline.bean.proto.DBgroup_info;
import com.bjtu.timeline.bean.require.GroupRequires.*;
import com.bjtu.timeline.bean.response.GroupResponses.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GroupMapper {

    @Select("select gid as id, gname as name from group_info " +
            "where muid = #{uid}")
    List<ListResponse.elm> selectListManage(int uid);

    @Select("select gid as id, gname as name from group_info " +
            "where gid in (" +
            "select gid from group_member where uid = #{uid}" +
            ")")
    List<ListResponse.elm> selectListIn(int uid);


    default int makeGroup(int uid, String name) {
        DBgroup_info rtv = new DBgroup_info(-1, uid, name);
        makeGroup(rtv);
        return rtv.getGid();
    }

    @Insert("insert into group_info(muid, gname) " +
            "values (#{body.muid},#{body.gname})")
    @Options(useGeneratedKeys = true, keyProperty = "body.gid")
    void makeGroup(@Param("body") DBgroup_info i);

    @Insert("insert into group_member " +
            "VALUES (#{gid},#{uid})")
    void addIntoGroup(int uid, int gid);

    @Delete("delete from group_info " +
            "where muid = #{uid} and gid = #{gid}")
    int deleteGroup(int uid, int gid);

    @Delete("delete from group_member " +
            "where gid = #{gid}")
    void cleanMember(int gid);

    @Delete("delete from group_apply " +
            "where gid = #{gid}")
    void cleanApplication(int gid);

    @Select("select count(*) from group_info " +
            "where gid = #{gid}")
    int checkGroup(int gid);

    @Select("select count(*) from group_member " +
            "where gid = #{gid} and uid = #{uid}")
    int checkMember(int uid, int gid);

    @Select("select count(*) from group_info " +
            "where gid = #{gid} and muid = #{uid}")
    int checkManager(int uid, int gid);

    @Insert("insert ignore into group_apply(uid, gid) " +
            "values (#{uid}, #{gid})")
    int doApply(int uid, int gid);

    @Delete("delete from group_apply " +
            "where gid = #{gid} and uid = #{uid}")
    int removeApply(int uid, int gid);

    @Delete("delete from group_member " +
            "where gid = #{gid} and uid = #{uid}")
    int removeMember(int uid, int gid);

    @Select("select * from group_info " +
            "where gid = #{gid}")
    DBgroup_info getInfo(int gid);

    @Select("select uid as id, nickname as name from user_info " +
            "where uid in (" +
            "select uid from group_member " +
            "where gid = #{gid}" +
            ")")
    List<InfoResponse.elmUser> getMemberOfGroup(int gid);

    @Select("select tid, title, begin, end, detail, type as `repeat`, gid as `group` from task " +
            "where gid = #{gid}")
    List<InfoResponse.elmTask> getTaskOfGroup(int gid);

    @Select("select uid as id, nickname as name from user_info " +
            "where uid in (" +
            "select uid from group_apply " +
            "where gid = #{gid}" +
            ")")
    List<InfoResponse.elmUser> getApplyList(int gid);

    @Update("update group_info " +
            "set gname = #{name} " +
            "where gid = #{gid}")
    void updateName(int gid, String name);

}
