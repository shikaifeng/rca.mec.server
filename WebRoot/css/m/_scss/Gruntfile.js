module.exports = function(grunt){

    // 项目配置
    grunt.initConfig({
        sass: {
            tmallbox: {
                files: {
                    '../tmallbox.css': 'tmallbox.scss'
                }
            },
            luckDraw: {
                files: {
                    '../luck_draw/the_voice_of_china.css': './luck_draw/the_voice_of_china.scss'
                }
            }
        },
        cssmin: {
            tmallbox: {
                files:{
                    '../tmallbox.min.css': '../tmallbox.css'
                }
            },
            luckDraw: {
                files:{
                    '../luck_draw/the_voice_of_china.min.css': '../luck_draw/the_voice_of_china.css'
                }
            }
        },
        watch: {
            tmallbox: {
                files: '*.scss',
                tasks: ['sass:tmallbox', 'cssmin:tmallbox']
            },
            luckDraw: {
                files: './luck_draw/*.scss',
                tasks: ['sass:luckDraw', 'cssmin:luckDraw']
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-watch');

    // 清理缓存
    grunt.registerTask('css', ['sass', 'cssmin', 'watch']);

   

}