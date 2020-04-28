<template>
  <a-card :bordered="false" :class="'cust-erp-sub-tab'">
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button v-if="mainId" @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <template
          slot="testStatus"
          slot-scope="text, record"
        >
					<span
            v-if="record.status === 0"
            class="testStatusButton"
            style="background-color: #f0ad4e;color: #fff"
          >未出勤</span>
          <span
            v-if="record.status === 2"
            class="testStatusButton"
            style="background-color: rgba(255,0,0,0.57);color: #fff"
          >已请假</span>
          <span
            v-if="record.status === 3"
            class="testStatusButton"
            style="background-color: rgba(255,0,0,0.57);color: #fff"
          >已迟到</span>
          <span
            v-if="record.status === 1"
            class="testStatusButton"
            style="background-color: #58b058;color: #fff"
          >已出勤</span>


        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleSignIn(record.id)">出勤</a>
          <a-divider type="vertical" />
          <a @click="handleNotSignIn(record.id)">未出勤</a>
          <a-divider type="vertical" />
          <a @click="handleLate(record.id)">迟到</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
        </span>

      </a-table>
    </div>

    <signInStudent-modal ref="modalForm" @ok="modalFormOk" :mainId="mainId"></signInStudent-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SignInStudentModal from './modules/SignInStudentModal'
  import { getAction } from '@/api/manage'


  export default {
    name: "SignInStudentList",
    mixins:[JeecgListMixin],
    components: { SignInStudentModal },
    props:{
      mainId:{
        type:String,
        default:'',
        required:false
      }
    },
    watch:{
      mainId:{
        immediate: true,
        handler(val) {
          if(!this.mainId){
            this.clearList()
          }else{
            this.queryParam['signInId'] = val
            this.loadData(1);
          }
        }
      }
    },
    data () {
      return {
        description: '签到表管理页面',
        disableMixinCreated:true,
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'学生账号',
            align:"center",
            dataIndex: 'studentCode'
          },
          {
            title:'学生名称',
            align:"center",
            dataIndex: 'studentName'
          },
          {
            title:'出勤状态',
            align:"center",
            scopedSlots: { customRender: 'testStatus' },
            dataIndex: 'status'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/signin/signIn/listSignInStudentByMainId",
          delete: "/signin/signIn/deleteSignInStudent",
          deleteBatch: "/signin/signIn/deleteBatchSignInStudent",
          signIn: "/signin/signIn/studentSignIn",
          notSignIn: "/signin/signIn/studentNotSignIn",
          late: "/signin/signIn/studentLate"
        },
        dictOptions:{
         orgCode:[],
        },

      }
    },
    methods: {
      clearList(){
        this.dataSource=[]
        this.selectedRowKeys=[]
        this.ipagination.current = 1
      },
      handleSignIn: function (id) {
        var that = this;
        getAction(that.url.signIn, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      handleNotSignIn: function (id) {
        var that = this;
        getAction(that.url.notSignIn, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      handleLate: function (id) {
        var that = this;
        getAction(that.url.late, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

  .testStatusButton {
    padding: 0.4em 0.4em;
    line-height: 0.72;
    text-align: center;
    border-radius: 4px;
    text-shadow: none;
    font-weight: normal;
    display: inline-block;
    overflow: hidden;
    height: 20px;
  }
</style>
