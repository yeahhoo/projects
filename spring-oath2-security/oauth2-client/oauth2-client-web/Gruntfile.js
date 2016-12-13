//todo replace jshint with jslint
//https://medium.com/@dan_abramov/lint-like-it-s-2015-6987d44c5b48#.xe8xpdapq
//https://github.com/quidmonkey/elisse/tree/feature/webpack

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
                 'src/main/resources/static/components/js/react-create-client.jsx',
                 'src/main/resources/static/components/js/react-create-user.jsx',
                 'src/main/resources/static/components/js/react-custom-error.jsx'
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
                   src: [
                       'react-create-client.jsx',
                       'react-create-user.jsx',
                       'react-custom-error.jsx'
                   ],
                   dest: 'target/classes/static/components/js',
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
