module.exports = function(grunt) {
  'use strict';

   // tell grunt to load jshint task plugin.
   grunt.loadNpmTasks('grunt-jsxhint');
   grunt.loadNpmTasks('grunt-babel');

   // configure tasks
   grunt.initConfig({
      jshint: {
          files: [
             'Gruntfile.js',
             'src/main/resources/static/components/**/*.js'
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
      }
   });

   grunt.registerTask('default', ['jshint', 'babel']);
};
