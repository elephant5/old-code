'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  ENV_CONFIG: '"dev"',
  API_ROOT: '"https://app.icolourful.cn"',
  PASSPORT_ROOT:'"https://passport.icolourful.cn"',
  PAY_ROOT:'"https://pay.icolourful.cn"'
})
