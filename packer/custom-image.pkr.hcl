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
  zone                = "us-east1-b"
  ssh_username        = "packer"
  image_name          = "centos8-java8-mysql-custom-image"
  # credentials_file      = "./target/csye-6225-dev-414805-613a7d5411b3.json"
  # service_account_email = "csye6225-packer-dev@csye-6225-dev-414805.iam.gserviceaccount.com"
  credentials_json      = "${github.secret.SERVICE_ACCOUNT_JSON}"
  service_account_email = "${github.secret.GCP_SERVICE_ACCOUNT_EMAIL}"

}

build {

  sources = ["source.googlecompute.centos8"]

  provisioner "shell" {
    script = "./packer/scripts/setup.sh"
  }

  provisioner "shell" {
    script = "./packer/scripts/create-user.sh"
  }

  provisioner "file" {
    source      = "./target/webapp-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/webapp-0.0.1-SNAPSHOT.jar"
  }

  provisioner "shell" {
    script = "./packer/scripts/change-ownership.sh"
  }
}