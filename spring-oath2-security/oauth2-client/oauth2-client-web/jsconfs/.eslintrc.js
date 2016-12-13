//list of rules: https://github.com/adrianhall/grumpy-wizards/blob/2016-01-26/.eslintrc.js

var OFF = 0, WARN = 1, ERROR = 2;
module.exports = exports = {

    "extends": ["eslint:recommended", "plugin:react/recommended"],
    "env": {
        "es6": true,
        "browser": true,
        "jquery": true
    },
    "ecmaFeatures": {
        "jsx": true,
        "modules": true
    },
    "parserOptions": {
        "sourceType": "module"
    },
    "rules": {
        "no-console": 0,
        "quotes": [ ERROR, "single" ],
        "strict": [ ERROR, "global" ]
    }

}