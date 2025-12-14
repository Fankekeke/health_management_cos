<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label="所属用户"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.userName"/>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="titleShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.title }}
              </template>
              {{ record.title.slice(0, 8) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="contentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.content }}
              </template>
              {{ record.content.slice(0, 30) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="eye" theme="twoTone" twoToneColor="#4a9ff5" @click="viewDetail(record)" title="查 看" style="margin-right: 8px;"></a-icon>
        </template>
      </a-table>
    </div>
    <a-modal
      title="睡眠详情"
      :visible="detailVisible"
      :footer="null"
      @cancel="detailVisible = false"
      width="700px">
      <div class="sleep-detail">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-descriptions bordered size="small" :column="1">
              <a-descriptions-item label="用户名称">{{ detailData.userName }}</a-descriptions-item>
              <a-descriptions-item label="记录日期">{{ detailData.recordDate }}</a-descriptions-item>
              <a-descriptions-item label="入睡时间">{{ detailData.startTime }}</a-descriptions-item>
              <a-descriptions-item label="起床时间">{{ detailData.endTime }}</a-descriptions-item>
              <a-descriptions-item label="总睡眠时长">
                <span v-if="detailData.durationMin">{{ (detailData.durationMin / 60).toFixed(1) }} 小时 ({{ detailData.durationMin }} 分钟)</span>
                <span v-else>- -</span>
              </a-descriptions-item>
              <a-descriptions-item label="夜间清醒次数">
                <span v-if="detailData.wakeCount !== null">{{ detailData.wakeCount }} 次</span>
                <span v-else>- -</span>
              </a-descriptions-item>
              <a-descriptions-item label="创建时间">{{ detailData.createDate }}</a-descriptions-item>
            </a-descriptions>
          </a-col>
          <a-col :span="12">
            <a-descriptions bordered size="small" :column="1">
              <a-descriptions-item label="深度睡眠">
                <span v-if="detailData.deepMin">{{ (detailData.deepMin / 60).toFixed(1) }} 小时 ({{ detailData.deepMin }} 分钟)</span>
                <span v-else>- -</span>
              </a-descriptions-item>
              <a-descriptions-item label="浅层睡眠">
                <span v-if="detailData.lightMin">{{ (detailData.lightMin / 60).toFixed(1) }} 小时 ({{ detailData.lightMin }} 分钟)</span>
                <span v-else>- -</span>
              </a-descriptions-item>
              <a-descriptions-item label="REM睡眠">
                <span v-if="detailData.remMin">{{ (detailData.remMin / 60).toFixed(1) }} 小时 ({{ detailData.remMin }} 分钟)</span>
                <span v-else>- -</span>
              </a-descriptions-item>
              <a-descriptions-item label="睡眠效率">
                <span v-if="detailData.efficiency !== null">{{ detailData.efficiency }}%</span>
                <span v-else>- -</span>
              </a-descriptions-item>
              <a-descriptions-item label="睡眠期间平均血氧">
                <span v-if="detailData.avgSleepSpo2 !== null">{{ detailData.avgSleepSpo2 }}%</span>
                <span v-else>- -</span>
              </a-descriptions-item>
              <a-descriptions-item label="最低血氧">
                <span v-if="detailData.lowestSpo2 !== null">{{ detailData.lowestSpo2 }}%</span>
                <span v-else>- -</span>
              </a-descriptions-item>
            </a-descriptions>
          </a-col>
        </a-row>
        <a-divider />
        <div class="advice-section">
          <h4>睡眠建议</h4>
          <div>{{ detailData.content || '暂无建议' }}</div>
        </div>
      </div>
    </a-modal>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Bulletin',
  components: {RangeDate},
  data () {
    return {
      detailVisible: false,
      detailData: {},
      advanced: false,
      bulletinAdd: {
        visiable: false
      },
      bulletinEdit: {
        visiable: false
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '用户名称',
        ellipsis: true,
        dataIndex: 'userName'
      }, {
        title: '用户头像',
        dataIndex: 'userImages',
        customRender: (text, record, index) => {
          if (!record.userImages) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.userImages.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.userImages.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '记录日期',
        ellipsis: true,
        dataIndex: 'recordDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '睡眠时间',
        dataIndex: 'startTime',
        ellipsis: true,
        customRender: (text, row, index) => {
          if (text !== null) {
            return row.startTime + ' - ' + row.endTime
          } else {
            return '- -'
          }
        }
      }, {
        title: '总睡眠时长',
        ellipsis: true,
        dataIndex: 'durationMin',
        customRender: (text, row, index) => {
          if (text !== null) {
            return (
              <a-tooltip placement="top">
                <template slot="title">
                  <div>总睡眠时长：{row.durationMin ? (row.durationMin / 60).toFixed(1) + '小时' : '- -'}</div>
                  <div>深度睡眠时长：{row.deepMin ? (row.deepMin / 60).toFixed(1) + '小时' : '- -'}</div>
                  <div>浅层睡眠时长：{row.lightMin ? (row.lightMin / 60).toFixed(1) + '小时' : '- -'}</div>
                  <div>REM睡眠时长：{row.remMin ? (row.remMin / 60).toFixed(1) + '小时' : '- -'}</div>
                </template>
                <span>{text}</span>
              </a-tooltip>
            );
          } else {
            return '- -';
          }
        }
      }, {
        title: '夜间清醒次数',
        ellipsis: true,
        dataIndex: 'wakeCount',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '次'
          } else {
            return '- -'
          }
        }
      }, {
        title: '睡眠期间平均血氧',
        ellipsis: true,
        dataIndex: 'avgSleepSpo2',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text + '%'
          } else {
            return '- -'
          }
        }
      }, {
        title: '建议',
        ellipsis: true,
        dataIndex: 'content',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    viewDetail(record) {
      this.detailData = {
        ...record,
        userName: record.userName || '- -',
        recordDate: record.recordDate || '- -',
        startTime: record.startTime || '- -',
        endTime: record.endTime || '- -',
        createDate: record.createDate || '- -',
        content: record.content || '暂无建议'
      };
      this.detailVisible = true;
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    add () {
      this.bulletinAdd.visiable = true
    },
    handleBulletinAddClose () {
      this.bulletinAdd.visiable = false
    },
    handleBulletinAddSuccess () {
      this.bulletinAdd.visiable = false
      this.$message.success('新增运动类型成功')
      this.search()
    },
    edit (record) {
      this.$refs.bulletinEdit.setFormValues(record)
      this.bulletinEdit.visiable = true
    },
    handleBulletinEditClose () {
      this.bulletinEdit.visiable = false
    },
    handleBulletinEditSuccess () {
      this.bulletinEdit.visiable = false
      this.$message.success('修改运动类型成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/sleep-records/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      this.$get('/cos/sleep-records/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
.sleep-detail {
  .advice-section {
    h4 {
      color: #595959;
      margin-bottom: 12px;
    }

    p {
      color: #262626;
      line-height: 1.6;
      margin: 0;
    }
  }

  :deep(.ant-descriptions-item-label) {
    background-color: #fafafa;
    font-weight: 500;
  }
}
@import "../../../../static/less/Common";
</style>
