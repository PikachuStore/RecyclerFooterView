package org.macpro.recyclerfooterview;

/**
 * 作者： macpro  on 2018/6/16.
 * 邮箱： xxx.com
 */
public enum LoadingState {

    // 加载中、加载完成、加载到底、加载出错了
    LOADING(1), LOAD_COMPLETE(2), LOAD_END(3), LOAD_ERROR(4);

    private int mState;

    LoadingState(int state) {
        this.mState = state;
    }

    public int getmState() {
        return mState;
    }
}
