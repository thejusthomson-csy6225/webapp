packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}

source "googlecompute" "centos8" {
  project_id          = "csye-6225-dev-414805"
  source_image_family = "centos-stream-8"
  zone                = "us-central1-a"
  ssh_username        = "packer"
  image_name          = "centos8-java8-mysql-custom-image"
}

build {

  sources = ["source.googlecompute.centos8"]



  provisioner "shell" {
    script = "scripts/setup.sh"
  }

  provisioner "shell" {
      script = "scripts/create-user.sh"
    }

  provisioner "file" {
    source      = "../target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/webapp-0.0.1-SNAPSHOT.jar"
  }

    provisioner "shell" {
      script = "scripts/change-ownership.sh"
    }


  // post-processor "googlecompute-import" {
  //   project_id  = "csye-6225-dev-414805"
  //   zone     = "us-east-a"
  //   machine_image_name = "centos8-custom-image"
  // }
}