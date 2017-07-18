package com.ailicai.app.ui.message.presenter;


import com.ailicai.app.common.reqaction.IwjwRespListener;
import com.ailicai.app.common.reqaction.ServiceSender;
import com.ailicai.app.common.utils.MyPreference;
import com.ailicai.app.message.NoticeRequest;
import com.ailicai.app.message.NoticeResponse;
import com.ailicai.app.ui.message.MessageFragment;

/**
 * 消息ListPresenter
 * Created by liyanan on 16/4/12.
 */
public class MessagePresenter {
    private static MessagePresenter presenter;
    private MessageFragment fragment;
    private boolean startLoad = true;

    private MessagePresenter() {

    }

    public static synchronized MessagePresenter getPresenter() {
        if (presenter == null) {
            presenter = new MessagePresenter();
        }
        return presenter;

    }


    public void setFragment(MessageFragment fragment) {
        this.fragment = fragment;
    }

    public void setStartLoad(boolean startLoad) {
        this.startLoad = startLoad;
    }

    public void requestNotice() {
        NoticeRequest noticeRequest = new NoticeRequest();
        if (!MyPreference.getInstance().read("get_agent_disturb_data_status", false)) {
            noticeRequest.setDataStatus(1);
        }
        ServiceSender.exec(fragment, noticeRequest, new IwjwRespListener<NoticeResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                if (fragment != null) {
                    fragment.showLoadView();
                }
            }

            @Override
            public void onJsonSuccess(NoticeResponse jsonObject) {
       /*         if (jsonObject.getHasPopActive() == 1 && fragment != null) {
                    FinanceAdActivity.showAdFullDialog(fragment.getActivity());
                }*/
                if (fragment != null && !fragment.isDetached()) {
                    fragment.showContentView();
                    fragment.requestSuccess(jsonObject);
                }
            }

            @Override
            public void onFailInfo(String errorInfo) {
                super.onFailInfo(errorInfo);
                if (fragment != null) {
                    fragment.showContentView();
                    fragment.requestFail(errorInfo);
                }
            }
        });
    }


    public synchronized void updateConversationWhenNeed(boolean forceUpdate) {
        if(fragment != null && fragment.getActivity() != null ) {

            if(forceUpdate) {
                updateConversation();
            } else {
                // 进来了证明有消息来了，需要首页其他tab切换到MessageFragment tab的时候刷新
                MessageFragment.indexSwitchNeedUpdateMessage = true;
                // 判断消息来得时候当前显示的是否首页消息列表页或者全局的消息列表页
               /* boolean isCurrentMessageActivity = fragment.getWRActivity() instanceof MessageActivity;
                boolean isCurrentIndexMessageFragment = fragment.getWRActivity() instanceof IndexActivity && ((IndexActivity) fragment.getWRActivity()).getCurrentPosition() == 2;
                if (isCurrentMessageActivity || isCurrentIndexMessageFragment) {
                    updateConversation();
                }*/
            }

        }
    }


    private void updateConversation() {

       /* List<EMConversation> conversations = getLocalConversation();
        //本地数据
        final List<ImAgent> localData = switchData(conversations);
        if (startLoad) {
            startLoad = false;
            if (remoteData != null) {
                remoteData.clear();
            }
            ImAgentListRequest request = new ImAgentListRequest();
            ServiceSender.exec(fragment, request, new IwjwRespListener<ImAgentListResponse>() {
                @Override
                public void onJsonSuccess(ImAgentListResponse jsonObject) {
                    if (jsonObject != null && jsonObject.getImAgentList() != null) {
                        if (fragment != null) {
                            //远程数据
                            remoteData = jsonObject.getImAgentList();
                            mergeData(localData, remoteData);
                        }
                    }
                }

                @Override
                public void onFailInfo(String errorInfo) {
                    super.onFailInfo(errorInfo);
                    mergeData(localData, remoteData);
                }
            });
        } else {
            mergeData(localData, remoteData);
        }*/
    }

    /**
     * 合并数据
     *
     * @param localData
     * @param remoteAgents
     * @return
     */
   /* private void mergeData(final List<ImAgent> localData, final List<ImAgent> remoteAgents) {
        // 合并数据同时把首页其他tab切换到MessageFragment重置成不需要刷新
        MessageFragment.indexSwitchNeedUpdateMessage = false;
        if (remoteAgents == null || remoteAgents.size() == 0) {
            fragment.updateList(localData);
            return;
        }
        if (localData == null || localData.size() == 0) {
            fragment.updateList(remoteAgents);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<ImAgent> finalData = new ArrayList<>();
                List<ImAgent> sameAgentList = new ArrayList<>();
                for (ImAgent remoteAgent : remoteAgents) {
                    ImAgent sameAgent = null;
                    for (ImAgent localAgent : localData) {
                        //if
                        if (remoteAgent.getUserType() == localAgent.getUserType()) {
                            //用户类型相同
                            if (remoteAgent.getAgentId() > 0 && remoteAgent.getAgentId() == localAgent.getAgentId()) {
                                //经纪人环信id相同
                                sameAgent = localAgent;
                            }
                            if (remoteAgent.getUserId() > 0 && remoteAgent.getUserId() == localAgent.getUserId()) {
                                //用户Id相同
                                sameAgent = localAgent;
                            }
                        }
                        //if
                    }
                    if (sameAgent != null) {
                        //需要合并
                        if (sameAgent.getLastMessageTime() >= remoteAgent.getLastMessageTime()) {
                            finalData.add(sameAgent);
                        } else {
                            remoteAgent.setNotReadMsgNum(sameAgent.getNotReadMsgNum());
                            finalData.add(remoteAgent);
                        }
                        sameAgentList.add(sameAgent);
                    } else {
                        finalData.add(remoteAgent);
                    }
                }
                localData.removeAll(sameAgentList);
                finalData.addAll(localData);
                Collections.sort(finalData, new TimeComparator());
                HandlerUtil.post(new Runnable() {
                    @Override
                    public void run() {
                        fragment.updateList(finalData);
                    }
                });
            }
        }).start();
    }*/

  /*   private String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

   private String getContent(Context context, ImAgent agent) {
        String content = null;
        switch (agent.getMsgType()) {
            case 1:
                //房源
                content = "[" + agent.getEstateName() + " " + agent.getPrice() + "]";
                break;
            case 2:
                //新房
                content = "[" + agent.getEstateName() + " " + agent.getPrice() + "]";
                break;
            case 3:
                //小区
                if (agent.getBizType() == 1) {
                    //出租
                    content = "[" + agent.getEstateName() + "]";
                } else {
                    //出售
                    content = "[" + agent.getEstateName() + " " + agent.getPrice() + "]";
                }
                break;
            case 4:
                //图片
                content = getString(context, R.string.picture);
                break;
            case 5:
                //位置
                content = getString(context, R.string.location);
                break;
            case 6:
                //语音
                content = getString(context, R.string.voice_prefix);
                break;
            case 7:
                //电话联系申请
                content = getString(context, R.string.call_request);
                break;
            case 8:
                //文本信息
                content = agent.getLastTxt();
                break;
            case 9:
                //催房租
                content = getString(context, R.string.urge_rent) + " 应付:" + agent.getPrice();
                break;
            case 10:
                //支付账单完成
                if (agent.getUserType() == 1) {
                    //聊天人为房东
                    //当前用户为租客
                    content = getString(context, R.string.pay_rent_result) + " 已付:" + agent.getPrice();
                } else if (agent.getUserType() == 2) {
                    //聊天人为租客
                    //当前用户为房东
                    content = getString(context, R.string.already_pay_rent) + " 到账:" + agent.getPrice();
                }
                break;
            default:
                content = "未能识别消息类型";
                break;
        }
        return content;
    }
*/
    /*public IMMessageGlobalNotify getIMMessageEvent(Context context, ImAgent agent) {

        // 是经纪人发过来的消息才会加入顶部显示队列
        if (agent.getUserType() != 1 && agent.getUserType() != 2) {
            String imgUri = "";
            String tvName = "";
            String tvContent = "";
            // 是经纪人type才会显示
            tvName = agent.getAgentName();
            imgUri = agent.getAgentPhotoUrl();
            tvContent = getContent(context, agent);
            IMMessageGlobalNotify imMessageGlobalNotify = new IMMessageGlobalNotify();
            imMessageGlobalNotify.setHeadIconUrl(imgUri);
            imMessageGlobalNotify.setMsgBody(tvContent);
            imMessageGlobalNotify.setMsgTitle(tvName);
            imMessageGlobalNotify.setImAgent(agent);
            return imMessageGlobalNotify;
        } else {
            return null;
        }
    }*/

    /**
     * 新消息到达时包装消息信息
     *
     * @param message
     * @return
     */
    /*public ImAgent initMessageData(EMMessage message) {
        ImAgent agent = new ImAgent();
        //agent.setNotReadMsgNum(conversation.getUnreadMsgCount());
        agent.setLastMessageTime(message.getMsgTime());
        String landlordId = getLandlordId(message);
        String renterId = getRenterId(message);
        String currentUserId = String.valueOf(UserInfo.getInstance().getUserId());
        int rentConstant = -1;
        if (TextUtils.isEmpty(landlordId)) {
        } else if (TextUtils.isEmpty(renterId)) {
        } else if (currentUserId.equals(landlordId)) {
            //当前用户是房东
            rentConstant = 1;
            agent.setUserId(Long.parseLong(getRenterId(message)));
            agent.setUserName(getRenterName(message));
            agent.setUserType(2);
            if (message.direct == EMMessage.Direct.RECEIVE) {
                //收到的消息,to为当前用户
                agent.setLandlordHxId(message.getTo());
                agent.setRenterHxId(message.getFrom());
            } else {
                //发送的消息,from为当前用户
                agent.setLandlordHxId(message.getFrom());
                agent.setRenterHxId(message.getTo());
            }
            agent.setRenterName(getRenterName(message));
            agent.setLandlordName(getLandloardName(message));
            if (agent.getUserId() == 0) {
                return null;
            }
        } else if (currentUserId.equals(renterId)) {
            //当前用户是租客
            rentConstant = 2;
            agent.setUserId(Long.parseLong(getLandlordId(message)));
            agent.setUserName(getLandloardName(message));
            agent.setUserType(1);
            if (message.direct == EMMessage.Direct.RECEIVE) {
                //收到的消息,to为当前用户
                agent.setLandlordHxId(message.getFrom());
                agent.setRenterHxId(message.getTo());
            } else {
                //发送的消息,from为当前用户
                agent.setLandlordHxId(message.getTo());
                agent.setRenterHxId(message.getFrom());
            }
            agent.setRenterName(getRenterName(message));
            agent.setLandlordName(getLandloardName(message));
            if (agent.getUserId() == 0) {
                return null;
            }
        } else {
        }
        if (rentConstant == -1) {
            //经纪人
            agent.setAgentId(getAgentId(message));
            agent.setAgentHxId(getAgentImId(message));
            agent.setAgentName(getAgentName(message));
            agent.setAgentPhotoUrl(getAgentAvatar(message));
            agent.setMendianName(getAgentStore(message));
            if (PreferenceManager.getInstance().isUserInNoDisturbList(getAgentImId(message))) {
                agent.setIsSetNotDisturb(1);
            } else {
                agent.setIsSetNotDisturb(0);
            }
            if (agent.getAgentId() == 0) {
                return null;
            }
        }
        EaseCommonUtils.setAgentData(message, fragment.getActivity(), rentConstant, agent);
        agent.setEstateDetail(getEatateDetail(message));
        return agent;
    }


    public List<ImAgent> switchData(List<EMConversation> conversations) {
        List<ImAgent> agents = new ArrayList<>();
        if (conversations == null || conversations.size() == 0) {
            return agents;
        }
        for (EMConversation conversation : conversations) {
            EMMessage message = conversation.getLastMessage();
            ImAgent agent = new ImAgent();
            agent.setNotReadMsgNum(conversation.getUnreadMsgCount());
            agent.setLastMessageTime(message.getMsgTime());
            String landlordId = getLandlordId(message);
            String renterId = getRenterId(message);
            String currentUserId = String.valueOf(UserInfo.getInstance().getUserId());
            int rentConstant = -1;
            if (TextUtils.isEmpty(landlordId)) {
            } else if (TextUtils.isEmpty(renterId)) {
            } else if (currentUserId.equals(landlordId)) {
                //当前用户是房东
                rentConstant = 1;
                agent.setUserId(Long.parseLong(getRenterId(message)));
                agent.setUserName(getRenterName(message));
                agent.setUserType(2);
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    //收到的消息,to为当前用户
                    agent.setLandlordHxId(message.getTo());
                    agent.setRenterHxId(message.getFrom());
                } else {
                    //发送的消息,from为当前用户
                    agent.setLandlordHxId(message.getFrom());
                    agent.setRenterHxId(message.getTo());
                }
                agent.setRenterName(getRenterName(message));
                agent.setLandlordName(getLandloardName(message));
                if (agent.getUserId() == 0) {
                    continue;
                }
            } else if (currentUserId.equals(renterId)) {
                //当前用户是租客
                rentConstant = 2;
                agent.setUserId(Long.parseLong(getLandlordId(message)));
                agent.setUserName(getLandloardName(message));
                agent.setUserType(1);
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    //收到的消息,to为当前用户
                    agent.setLandlordHxId(message.getFrom());
                    agent.setRenterHxId(message.getTo());
                } else {
                    //发送的消息,from为当前用户
                    agent.setLandlordHxId(message.getTo());
                    agent.setRenterHxId(message.getFrom());
                }
                agent.setRenterName(getRenterName(message));
                agent.setLandlordName(getLandloardName(message));
                if (agent.getUserId() == 0) {
                    continue;
                }
            } else {
            }
            if (rentConstant == -1) {
                //经纪人
                agent.setAgentId(getAgentId(message));
                agent.setAgentHxId(getAgentImId(message));
                agent.setAgentName(getAgentName(message));
                agent.setAgentPhotoUrl(getAgentAvatar(message));
                agent.setMendianName(getAgentStore(message));
                if (PreferenceManager.getInstance().isUserInNoDisturbList(getAgentImId(message))) {
                    agent.setIsSetNotDisturb(1);
                } else {
                    agent.setIsSetNotDisturb(0);
                }
                if (agent.getAgentId() == 0) {
                    continue;
                }
            }
            EaseCommonUtils.setAgentData(message, fragment.getActivity(), rentConstant, agent);
            agent.setEstateDetail(getEatateDetail(message));
            agents.add(agent);
        }
        return agents;
    }


    public List<EMConversation> getLocalConversation() {
        List<EMConversation> data = ConversationUtil.loadConversationList();
        if (data == null || data.size() == 0) {
            return null;
        }
        int size = data.size();
        final ArrayList<EMConversation> newArrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            EMConversation conversation = data.get(i);
            List<EMMessage> emMessages = conversation.getAllMessages();
            emMessages = sortAndClearList(emMessages.toArray(new EMMessage[emMessages.size()]));
            EMMessage lastMessage = null;
            int total = emMessages.size();
            for (int j = total - 1; j >= 0; j--) {
                lastMessage = emMessages.get(j);
                if (lastMessage.getType() == EMMessage.Type.TXT) {
                    int cusMsgType = EaseCommonUtils.getCustomMessageType(lastMessage);
                    if (cusMsgType == EaseConstant.MESSAGE_ATTR_TYPE_TAG_VIEW) {
                        //空消息不显示在会话中
                        continue;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (lastMessage != null) {
                newArrayList.add(conversation);
            }
        }
        return newArrayList;
    }

    private List<EMMessage> sortAndClearList(EMMessage[] messages) {
        if (null != messages && messages.length > 0) {
            Arrays.sort(messages, new sortClass());
            EMMessage[] result = clearList(messages);
            return Arrays.asList(result);
        }
        return new ArrayList<>();
    }*/

    /**
     * 判断是否是命令消息
     *
     * @param message
     * @return
     */
    /*private boolean isTagMsg(EMMessage message) {
        int customMsgType = EaseCommonUtils.getExAttrInt(message, EaseConstant.MESSAGE_ATTR_TYPE);
        return EaseConstant.MESSAGE_ATTR_TYPE_TAG_VIEW == customMsgType;
    }


    //去除数组中重复的记录
    public EMMessage[] clearList(EMMessage[] a) {
        List<EMMessage> list = new LinkedList<EMMessage>();
        for (int i = 0; i < a.length; i++) {
            EMMessage currentMsg = a[i];
            if (i < a.length - 1) {
                EMMessage nextMsg = a[i + 1];
                if (nextMsg.getMsgId().equalsIgnoreCase(currentMsg.getMsgId())) {
                    continue;
                } else {
                    if (!isTagMsg(currentMsg)) {
                        list.add(currentMsg);
                    }
                }
            } else {
                if (!isTagMsg(currentMsg)) {
                    list.add(currentMsg);
                }
            }
        }

        return list.toArray(new EMMessage[list.size()]);
    }*/

    /**
     * 获取经纪人id
     *
     * @param message
     * @return
     */
    /*private long getAgentId(EMMessage message) {
        long agentId = 0;
        try {
            //经纪人id
            agentId = Long.parseLong(message.getStringAttribute(EaseConstant.MESSAGE_ATTR_HOUSE_AGENT_IWJW_ID));
        } catch (EaseMobException e) {
//            e.printStackTrace();
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }
        return agentId;
    }

    *//**
     * 获取经纪人phone
     *
     * @param message
     * @return
     *//*
    private String getAgentPhone(EMMessage message) {
        String agentPhone = "";
        try {
            //经纪人电话
            agentPhone = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_HOUSE_AGENTPHONE);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return agentPhone;
    }

    *//**
     * 获取经纪人name
     *
     * @param message
     * @return
     *//*
    private String getAgentName(EMMessage message) {
        String agentName = "";
        try {
            //经纪人名称
            agentName = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_HOUSE_AGENTNAME);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return agentName;
    }

    *//**
     * 获取经纪人头像
     *
     * @param message
     * @return
     *//*
    private String getAgentAvatar(EMMessage message) {
        String agentAvatar = "";
        try {
            //经纪人头像
            agentAvatar = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_HOUSE_AGENTAVATAR);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return agentAvatar;
    }

    *//**
     * 获取经纪人门店
     *
     * @param message
     * @return
     *//*
    private String getAgentStore(EMMessage message) {
        String agentStore = "";
        try {
            //经纪人门店
            agentStore = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_HOUSE_AGENT_STORE);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return agentStore;
    }

    *//**
     * 获取经纪人对应的环信用户id
     *
     * @param message
     * @return
     *//*
    private String getAgentImId(EMMessage message) {
        //环信用户id
        String agentImId;
        if (message.direct == EMMessage.Direct.RECEIVE) {
            //收到的消息
            agentImId = message.getFrom();
        } else {
            //发送的消息
            agentImId = message.getTo();
        }
        return agentImId;
    }

    *//**
     * 获取agentId与agentImId的映射map
     *
     * @param conversations
     * @return
     *//*
    public Map<Long, String> getAgentIDMap(List<EMConversation> conversations) {
        if (conversations == null || conversations.size() == 0) {
            return null;
        }
        HashMap<Long, String> map = new HashMap<>();
        for (EMConversation conversation : conversations) {
            EMMessage emMessage = conversation.getLastMessage();
            map.put(getAgentId(emMessage), getAgentImId(emMessage));
        }
        return map;
    }

    *//**
     * 获取房东id
     *
     * @param message
     * @return
     *//*
    private String getLandlordId(EMMessage message) {
        String landlordId = "";
        try {
            landlordId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_MSG_LANDLOARD_ID);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return landlordId;
    }

    *//**
     * 获取租客id
     *
     * @param message
     * @return
     *//*
    private String getRenterId(EMMessage message) {
        String renterId = "";
        try {
            renterId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_MSG_RENTER_ID);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return renterId;
    }

    *//**
     * 获取房东名称
     *
     * @param message
     * @return
     *//*
    private String getLandloardName(EMMessage message) {
        String renterId = "";
        try {
            renterId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_MSG_LANDLOARD_NAME);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return renterId;
    }

    *//**
     * 获取租客名称
     *
     * @param message
     * @return
     *//*
    private String getRenterName(EMMessage message) {
        String renterId = "";
        try {
            renterId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_MSG_RENTER_NAME);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return renterId;
    }

    *//**
     * 获取小区名称
     *
     * @param message
     * @return
     *//*
    private String getEatateName(EMMessage message) {
        String renterId = "";
        try {
            renterId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_MSG_ESTATE_NAME);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return renterId;
    }

    *//**
     * 获取小区详情
     *
     * @return
     *//*
    private String getEatateDetail(EMMessage message) {
        String renterId = "";
        try {
            renterId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_MSG_ESTATE_DETAIL);
        } catch (EaseMobException e) {
//            e.printStackTrace();
        }
        return renterId;
    }
*/
    public void removeFragment() {
        presenter = null;
        startLoad = true;
    }
/*
static class TimeComparator implements Comparator<ImAgent> {
    public int compare(ImAgent agent1, ImAgent agent2) {
        if (agent1.getLastMessageTime() > agent2.getLastMessageTime()) {
            return -1;
        } else if (agent1.getLastMessageTime() == agent2.getLastMessageTime()) {
            return 0;
        } else {
            return 1;
        }
    }
}

public class sortClass implements Comparator {
    public int compare(Object arg0, Object arg1) {
        long leftTime = ((EMMessage) arg0).getMsgTime();
        long rightTime = ((EMMessage) arg1).getMsgTime();
        if (rightTime > leftTime) {
            return -1;
        } else if (rightTime < leftTime) {
            return 1;
        } else {
            return 0;
        }
    }

}
*/
}
