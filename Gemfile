# frozen_string_literal: true

source "https://rubygems.org"
gem 'fastlane', '~> 2'
gem 'fastlane-plugin-wpmreleasetoolkit', '~> 9.0'

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
