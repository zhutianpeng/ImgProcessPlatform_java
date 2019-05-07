//数据面板子组件
var DataBoard = {
    template: `<data-box :title="''" :dheight="800">
                    <data-box :title="'健身数据面板'" :dheight="400" :icon="'account'" :boxb="false">

                        <ve-line :data="pieChart.data" :extend="pieChart.extend" :height="'350px'"></ve-line>
                    </data-box>
                    <data-box :title="'健身数据面板'" :dheight="350" :icon="'account'" :boxb="false">
                        <ve-line :data="lineChart.data" :extend="lineChart.extend"></ve-line>
                    </data-box>
                </data-box>`,
    data:function () {
        return {
            pieChart: {
                extend:{
                    legend:{
                        textStyle: {color: '#fff'},
                    },
                    grid: {
                        textStyle: {
                            color: "#fff"
                        }
                    }
                },
                data: {
                    columns: ['日期', '销售额'],
                    rows: [
                        {'日期': '1月1日', '销售额': 123},
                        {'日期': '1月2日', '销售额': 1223},
                        {'日期': '1月3日', '销售额': 2123},
                        {'日期': '1月4日', '销售额': 4123},
                        {'日期': '1月5日', '销售额': 3123},
                        {'日期': '1月6日', '销售额': 7123}
                    ]
                }
            },
            lineChart: {
                extend:{
                    textStyle: {color: '#fff'},
                },
                data: {
                    columns: ['日期', '销售额'],
                    rows: [
                        {'日期': '1月1日', '销售额': 123},
                        {'日期': '1月2日', '销售额': 1223},
                        {'日期': '1月3日', '销售额': 2123},
                        {'日期': '1月4日', '销售额': 4123},
                        {'日期': '1月5日', '销售额': 3123},
                        {'日期': '1月6日', '销售额': 7123}
                    ]
                }
            },
        }
    },
    components:{
        dataBox: DataBox
    }
};

var ExeIntro = {
    template: `<div>健身指导介绍文字</div>`,
};