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

<!--        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>-->
<!--        </a-form-item>-->
<!--        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--          <a-input v-decorator="[ 'sysOrgCode', validatorRules.sysOrgCode]" placeholder="请输入所属部门"></a-input>-->
<!--        </a-form-item>-->
        <a-form-item label="签到单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'code', validatorRules.code]" placeholder="请输入签到单号"></a-input>
        </a-form-item>
        <a-form-item label="签到发起时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择签到发起时间" v-decorator="[ 'time', validatorRules.time]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: "SignInModal",
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
        validatorRules:{
        createBy:{},
        sysOrgCode:{},
        code:{},
        time:{},
        },
        url: {
          add: "/signin/signIn/add",
          edit: "/signin/signIn/edit",
          Num: '/sys/fillRule/executeRuleByCode/sign_in',
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
          this.form.setFieldsValue(pick(this.model,'createBy','sysOrgCode','code','time'))
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
        this.form.setFieldsValue(pick(row,'createBy','sysOrgCode','code','time'))
      },


    }
  }
</script>