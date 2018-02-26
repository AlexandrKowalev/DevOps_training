#task4.1
Vagrant.configure("2") do |config|
  config.vm.box = "bertvv/centos72"
  config.vm.provision "shell", inline: <<-SHELL
    yum install -y mc
    systemctl stop firewalld
    systemctl disable firewalld
  SHELL
  config.vm.provider "virtualbox" do |vb|
    vb.gui = true
    vb.memory = 1024
  end
  
  config.vm.define "apache" do |apache|
    apache.vm.hostname="apache"
    apache.vm.network "private_network", ip: "192.168.0.10"
    apache.vm.network "forwarded_port", guest: 80, host: 8009
    apache.vm.provision "shell", inline: <<-SHELL
    apache.memory = 1024
      echo -e "192.168.0.11 tomcat1 tomcat1
        192.168.0.12 tomcat2 tomcat2" >> /etc/hosts

      yum install java-1.8.0-openjdk-devel -y
      sudo cp /etc/profile /etc/profile_backup
      echo 'export JAVA_HOME=/usr/lib/jvm/jre-1.8.0-openjdk' | sudo tee -a /etc/profile
      echo 'export JRE_HOME=/usr/lib/jvm/jre' | sudo tee -a /etc/profile
      source /etc/profile

      wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat/jenkins.repo -y
      rpm --import https://jenkins-ci.org/redhat/jenkins-ci.org.key -y
      yum install jenkins -y

      yum install httpd -y
      systemctl enable httpd
      cp /vagrant/mod_jk.so /etc/httpd/modules/
      echo -e "worker.list=lb
        worker.lb.type=lb
        worker.lb.balance_workers=tomcat1, tomcat2
        worker.tomcat1.host=192.168.0.11
        worker.tomcat1.port=8009
        worker.tomcat1.type=ajp13
        worker.tomcat2.host=192.168.0.12
        worker.tomcat2.port=8009
        worker.tomcat2.type=ajp13" > /etc/httpd/conf/workers.properties

      echo -e "LoadModule jk_module modules/mod_jk.so
        JkWorkersFile conf/workers.properties
        JkShmFile /tmp/shm
        JkLogFile logs/mod_jk.log
        JkLogLevel info
        JkMount /test* lb" >> /etc/httpd/conf/httpd.conf
      systemctl start httpd
      yum install -y git
      git clone https://github.com/vitali-zevako/gradleSample.git
      
      
    SHELL
  end
  
  config.vm.define "tomcat1" do |tomcat1|
    tomcat1.vm.hostname = "tomcat1"
    tomcat1.vm.network "private_network", ip: "192.168.0.11"
    tomcat1.vm.provision "shell", inline:<<-SHELL
      echo -e "192.168.0.10 apache apache
        192.168.0.12 tomcat2 tomcat2" >> /etc/hosts
      yum install java-1.8.0-openjdk -y
      yum install tomcat tomcat-webapps tomcat-admin-webapps -y
      mkdir /usr/share/tomcat/webapps/test
      touch /usr/share/tomcat/webapps/test/index.html
      echo -e "tomcat1" >> /usr/share/tomcat/webapps/test/index.html
      systemctl enable tomcat
      systemctl start tomcat
    SHELL
  end

  config.vm.define "tomcat2" do |tomcat2|
    tomcat2.vm.hostname = "tomcat2"
    tomcat2.vm.network "private_network", ip: "192.168.0.12"
    tomcat2.vm.provision "shell", inline:<<-SHELL
      echo -e "192.168.0.10 apache apache
        192.168.0.11 tomcat1 tomcat1" >> /etc/hosts
      yum install java-1.8.0-openjdk -y
      yum install tomcat tomcat-webapps tomcat-admin-webapps -y
      mkdir /usr/share/tomcat/webapps/test
      touch /usr/share/tomcat/webapps/test/index.html
      echo -e "tomcat2" >> /usr/share/tomcat/webapps/test/index.html
      systemctl enable tomcat
      systemctl start tomcat
    SHELL
  end

end