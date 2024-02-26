
packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}

source "googlecompute" "centos8" {
   project_id            = var.project_id
  source_image_family   = var.source_image_family
  zone                  = var.zone
  ssh_username          = var.ssh_username
  image_name            = var.image_name
  network               = var.network
  service_account_email = var.service_account_email

}

build {

  sources = ["source.googlecompute.centos8"]

  provisioner "shell" {
    script = "${var.scripts_path}setup.sh"
  }

  provisioner "shell" {
    script = "${var.scripts_path}create-user.sh"
  }

  provisioner "file" {
    source      = "${var.jar_source}"
    destination = "${var.jar_destination}
  }

  provisioner "shell" {
    script = "${var.scripts_path}change-ownership.sh"
  }

  provisioner "file" {
    source = "${var.scripts_path}webapp-launch.service"
    destination = "/tmp/webapp-launch.service"
  }

  provisioner "shell" {
    script = "${var.scripts_path}execute-sysd.sh"
  }

  provisioner "file" {
    source = "./.env"
    destination = "/tmp/.env"
  }
}
