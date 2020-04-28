<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="请假单号">
              <a-input placeholder="请输入请假单号" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <a-button type="primary" icon="download" @click="handleExportXls('请假发起表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-button v-if="selectedRowKeys.length > 0" @click="pass" type="primary" icon="check">审批通过</a-button>
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="tableScroll"
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
          >未通过</span>
          <span
            v-if="record.status === 1"
            class="testStatusButton"
            style="background-color: #58b058;color: #fff"
          >已通过</span>

        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <askForLeave-modal ref="modalForm" @ok="modalFormOk"></askForLeave-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AskForLeaveModal from './modules/AskForLeaveModal'
  import { getAction } from '@/api/manage'
  export default {
    name: "AskForLeaveList",
    mixins:[JeecgListMixin],
    components: {
      AskForLeaveModal
    },
    data () {
      return {
        description: '请假发起表管理页面',
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
            title:'请假单号',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'审核状态',
            align:"center",
            scopedSlots: { customRender: 'testStatus' },
            dataIndex: 'status'
          },
          {
            title:'申请学生姓名',
            align:"center",
            dataIndex: 'studentName'
          },
          {
            title:'请假原因',
            align:"center",
            dataIndex: 'reason'
          },
          {
            title:'请假开始日期',
            align:"center",
            dataIndex: 'startTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'请假结束日期',
            align:"center",
            dataIndex: 'endTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/askforleave/askForLeave/list1",
          delete: "/askforleave/askForLeave/delete",
          deleteBatch: "/askforleave/askForLeave/deleteBatch",
          exportXlsUrl: "/askforleave/askForLeave/exportXls",
          importExcelUrl: "askforleave/askForLeave/importExcel",
          pass: "/askforleave/askForLeave/pass"
        },
        dictOptions:{},
        tableScroll:{x :6*147+50}
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      pass: function () {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认审批通过",
            content: "是否审批通过以下请假请求?",
            onOk: function () {
              getAction(that.url.pass, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              })

            }
            }
          );
        }
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