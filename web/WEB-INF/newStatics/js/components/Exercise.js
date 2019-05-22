//数据面板子组件
var DataBoard = {
    template: `<data-box :title="'关键关节点置信度统计'" :dheight="800">
                    <ve-bar :data="poseChart.data" :extend="poseChart.extend"></ve-bar>       
               </data-box>`,
    props:['pose'],
    data:function () {
        return {
            pointMapping:{
                0:'鼻子',
                1:'脖子',
                2:'右肩',
                3:'右肘',
                4:'右腕',
                5:'左肩',
                6:'左肘',
                7:'左腕',
                8:'右臀',
                9:'右膝',
                10:'右踝',
                11:'左臀',
                12:'左膝',
                13:'左踝',
                14:'右眼',
                15:'左眼',
                16:'右耳',
                17:'左耳'
            },
            poseChart: {
                extend:{
                    legend:{
                        textStyle: {color: '#fff'},
                    },
                    grid: {
                        textStyle: {
                            color: "#fff"
                        }
                    },
                    xAxis:{
                        axisLine:{
                            lineStyle:{
                                color:'#fff'
                            }
                        }
                    },
                    yAxis:{
                        axisLine:{
                            lineStyle:{
                                color:'#fff'
                            }
                        }
                    }
                },
                data: {
                    columns: ['关节点名称', '置信度'],
                    rows: []
                }
            }
        }
    },
    components:{
        dataBox: DataBox
    },
    watch:{
        pose:{
            deep: true,
            handler: function (newVal, oldVal) {
                this.refreshData(newVal);
            }
        }
    },
    methods:{
        refreshData: function (poseData) {
            let poseTemp = []; //该数组用于暂存rows数据
            for(let i = 0; i < 18; i++){
                if(poseData[i]){
                    poseTemp.push({
                        '关节点名称': this.pointMapping[i],
                        '置信度': poseData[i]
                    })
                }else{
                    poseTemp.push({
                        '关节点名称': this.pointMapping[i],
                        '置信度': 0
                    })
                }
            }
            this.poseChart.data.rows = poseTemp;
        }
    }
};

var ExeIntro = {
    template: `<div>健身指导介绍文字</div>`,
};
