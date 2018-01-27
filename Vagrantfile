Vagrant.configure("2") do |config|
  config.vm.box = "bertvv/centos72"
  config.vm.provision "shell", inline: <<-SHELL
      yum install -y mc
  SHELL
  config.vm.provider "virtualbox" do |vb|
    vb.gui = true
  end
  
  config.vm.define "server1" do |server1|
    server1.vm.hostname="server1"
    server1.vm.network "private_network", ip: "192.168.0.10"
    server1.vm.provision "shell", inline: <<-SHELL
      echo '192.168.0.11 server2 server2' >> /etc/hosts
      yum install -y git
      git init 
      git clone https://github.com/AlexandrKowalev/DevOps_training.git -b task2 
      cat /home/vagrant/DevOps_training/task2.txt
    SHELL
  end
  
  config.vm.define "server2" do |server2|
    server2.vm.hostname="server2"
    server2.vm.network "private_network", ip: "192.168.0.11"
    server2.vm.provision "shell", inline: <<-SHELL
      echo '192.168.0.10 server1 server1' >> /etc/hosts
    SHELL
    
  end
   
end
