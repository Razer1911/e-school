<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="请假单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'code', validatorRules.code]" placeholder="请输入请假单号"></a-input>
        </a-form-item>
<!--        <a-form-item label="审核教师" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <a-input v-decorator="[ 'teacherName', validatorRules.teacherName]" placeholder="请输入审核教师"></a-input>-->
<!--        </a-form-item>-->
        <a-form-item label="请假原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'reason', validatorRules.reason]" placeholder="请输入请假原因"></a-input>
        </a-form-item>
        <a-form-item label="请假开始日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择请假开始日期" v-decorator="[ 'startTime', validatorRules.startTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="请假结束日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择请假结束日期" v-decorator="[ 'endTime', validatorRules.endTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: "AskForLeaveModal",
    components: {
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          code: {rules: [
          ]},
          teacherName: {rules: [
          ]},
          reason: {rules: [
          ]},
          startTime: {rules: [
          ]},
          endTime: {rules: [
          ]},
        },
        url: {
          add: "/askforleave/askForLeave/add",
          edit: "/askforleave/askForLeave/edit",
          Num: '/sys/fillRule/executeRuleByCode/ask_for_leave',
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      getNum() {
        httpAction(this.url.Num, this.model, 'put').then(res => {
          // 执行成功，获取返回的值，并赋到页面上
          if (res.success) {
            this.model.code = res.result
            this.$nextTick(() => {
              this.form.setFieldsValue({ code: this.model.code})
            })
          }
        })
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        if (!this.model.id) {
          this.getNum()
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'code','status','teacherName','reason','startTime','endTime'))
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'code','status','teacherName','reason','startTime','endTime'))
      },


    }
  }
</script>