//todo replace jshint with jslint
//https://medium.com/@dan_abramov/lint-like-it-s-2015-6987d44c5b48#.xe8xpdapq
//https://github.com/quidmonkey/elisse/tree/feature/webpack

module.exports = function(grunt) {
    'use strict';

    // tell grunt to load jshint task plugin.
    grunt.loadNpmTasks('grunt-webpack');

    // configure tasks
    grunt.initConfig({
        webpack: {
            someTarget: require("./jsconfs/webpack.config.js")
        }
   });

   grunt.registerTask('default', ['webpack']);

};
