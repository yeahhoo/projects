//todo replace jshint with jslint
//https://medium.com/@dan_abramov/lint-like-it-s-2015-6987d44c5b48#.xe8xpdapq

module.exports = function(grunt) {
    'use strict';

    // tell grunt to load jshint task plugin.
    grunt.loadNpmTasks('grunt-jsxhint');
    grunt.loadNpmTasks('grunt-babel');
    grunt.loadNpmTasks('grunt-webpack');

    // configure tasks
    grunt.initConfig({
        jshint: {
             files: [
                 'Gruntfile.js',
                 'src/main/resources/static/components/js/containers/nav-container.js',
                 //'src/main/resources/static/components/js/**/*.js',
                 'src/main/resources/static/components/**/*.jsx'
            ],
            options: {
                jshintrc: '.jshintrc',
                ignores: [
                   'src/main/resources/static/libs/**/*.js'
                ]
            }
        },
        babel: {
           options: {
               plugins: ['transform-react-jsx', 'transform-strict-mode'],
               presets: ['es2015', 'react']
           },
           jsx: {
               files: [{
                   expand: true,
                   cwd: 'src/main/resources/static/components/js',
                   src: ['*.jsx'],
                   dest: 'target/classes/static/components/js/compiled',
                   ext: '.js'
               }]
           }
        },
        webpack: {
            someTarget: require("./webpack.config.js")
        }
   });

   grunt.registerTask('default', ['jshint', 'babel', 'webpack']);

};
