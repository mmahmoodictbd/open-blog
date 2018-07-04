### Directory Layout

```
inventories/
   osboxes.hosts                        # Inventory file for test VM osboxes

deploy-openblog-playbook.yaml           # Playbook


roles/                                  #
   common/                              # This hierarchy represents a "role"
      tasks/                            #
         main.yml.sample                #
      handlers/                         #
         main.yml.sample                #
      templates/                        #
         main.conf.j2.sample            #
      files/                            #
         file.sample                    #
      vars/                             #
         main.yml.sample                #
      defaults/                         #
         main.yml.sample                #
      meta/                             #
         main.yml.sample                #
    
   os/                                  # 
      tasks/                            #
         main.yml                       # Install ufw, emacs, git and maven
   apache/                              #
      tasks/                            #
         main.yml                       # Install and configure apache
   mysql/                               #
      tasks/                            #
         main.yml                       # Install and configure MySQL
   java/                                #
      tasks/                            #
         main.yml                       # Install Oracle Java
   javaapp/                             #
      tasks/                            #
         main.yml                       # Create SSH user, MySQL user and App Database
     
```
### How to run
`ansible-playbook -i inventories/osboxes.hosts deploy-openblog-playbook.yaml --ask-sudo-pass --ask-vault-pass`