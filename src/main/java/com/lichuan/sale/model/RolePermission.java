package com.lichuan.sale.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "role_permission")
public class RolePermission {
    @Id
    private Integer rpid;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private Long permissionId;

    /**
     * 备注：例如：（系统管理员 新增 删除用户 权限）
     */
    private String note;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_uid")
    private Long updateUid;

    /**
     * @return rpid
     */
    public Integer getRpid() {
        return rpid;
    }

    /**
     * @param rpid
     */
    public void setRpid(Integer rpid) {
        this.rpid = rpid;
    }

    /**
     * 获取角色id
     *
     * @return role_id - 角色id
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置角色id
     *
     * @param roleId 角色id
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取权限id
     *
     * @return permission_id - 权限id
     */
    public Long getPermissionId() {
        return permissionId;
    }

    /**
     * 设置权限id
     *
     * @param permissionId 权限id
     */
    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    /**
     * 获取备注：例如：（系统管理员 新增 删除用户 权限）
     *
     * @return note - 备注：例如：（系统管理员 新增 删除用户 权限）
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置备注：例如：（系统管理员 新增 删除用户 权限）
     *
     * @param note 备注：例如：（系统管理员 新增 删除用户 权限）
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取修改人
     *
     * @return update_uid - 修改人
     */
    public Long getUpdateUid() {
        return updateUid;
    }

    /**
     * 设置修改人
     *
     * @param updateUid 修改人
     */
    public void setUpdateUid(Long updateUid) {
        this.updateUid = updateUid;
    }
}