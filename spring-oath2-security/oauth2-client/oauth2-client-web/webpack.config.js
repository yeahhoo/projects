//https://advancedweb.hu/2016/02/02/the-first-steps-from-grunt-to-webpack/
//http://stackoverflow.com/questions/33460420/babel-loader-jsx-syntaxerror-unexpected-token
// this best
//https://www.twilio.com/blog/2015/08/setting-up-react-for-es6-with-webpack-and-babel-2.html

module.exports = {
	entry: './src/main/resources/static/components/js/react-main-webpack.jsx',
	output: {
		path: './target/classes/static/components/js/webpack',
		filename: 'bundle.js'
	},

	resolve: {
        modulesDirectories: ["node_modules"],
        extensions: ['', '.js', '.jsx', '.json'],
    },

	module: {
	    preLoaders: [
	        // Javascript
            { test: /\.js[x]?$/, loader: 'eslint', exclude: /node_modules/ }
        ],
		loaders: [
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'react']
                }
            }
		]
	},
	eslint: {
	    configFile: 'jsconfs/.eslintrc.js',
	    failOnWarning: false,
        failOnError: true
    },
	debug: true
};