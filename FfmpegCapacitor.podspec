
  Pod::Spec.new do |s|
    s.name = 'FfmpegCapacitor'
    s.version = '0.0.1'
    s.summary = 'A plugin for capacitor that lets you execute ffmpeg commands on the device natively'
    s.license = 'MIT'
    s.homepage = 'git@github.com:karimdjemai/ffmpeg-capacitor.git'
    s.author = 'karimdjemai'
    s.source = { :git => 'git@github.com:karimdjemai/ffmpeg-capacitor.git', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end