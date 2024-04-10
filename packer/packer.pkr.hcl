variable "project_id" {
  type        = string
  description = "Google Cloud Project ID."
}

variable "source_image_family" {
  type        = string
  description = "Source image family for the custom image."
}

variable "zone" {
  type        = string
  description = "Google Cloud Zone for the custom image."
}

variable "ssh_username" {
  type        = string
  description = "SSH username for connecting to the instance."
}

variable "image_name" {
  type        = string
  description = "Name of the custom image to be created."
}

variable "scripts_path" {
  type        = string
  description = "Path to the directory containing provisioning scripts."
}

variable "jar_source" {
  type        = string
  description = "Path to the JAR file to be copied to the instance."
}

variable "jar_destination" {
  type        = string
  description = "Destination path for the JAR file on the instance."
}

variable "network" {
  type        = string
  description = "Network type of the VM"
}

variable "service_account_email" {
  type        = string
  description = "Service account mail"
}
post-processor "manifest" {
    output     = "manifest.json"
    strip_path = true
  }