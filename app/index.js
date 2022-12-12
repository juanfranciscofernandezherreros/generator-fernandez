"use strict";
const Generator = require("yeoman-generator");
const chalk = require("chalk");
const yosay = require("yosay");
const lodash = require("lodash");

module.exports = class extends Generator {
  prompting() {

	this.log("                                                        ");
	this.log("..####...#####...######..##..##...####...#####...######.");
	this.log(".##..##..##..##..##......###.##..##..##..##..##....##...");
	this.log(".##..##..#####...####....##.###..######..#####.....##...");
	this.log(".##..##..##......##......##..##..##..##..##........##...");
	this.log("..####...##......######..##..##..##..##..##......######.");
	this.log("........................................................");
	this.log("                            GENERATOR - v 0.0.1         ");
	this.log("                                                        ");

    const prompts = [
      {
        type: "input",
        name: "isSix",
        message:
          "What is the standard you want to use? (use SIX or DARWIN. Note: is uppercase)",
        default: "SIX"
      },
      {
        type: "input",
        name: "resource",
        message:
          "What is the RESOURCE that you want to model? (your API should be /resource/{id})",
        default: "crud-resource"
      },
      {
        type: "input",
        name: "apiType",
        message:
          "What is the JSON:API type of your resource? (please, use plural!)",
        default: "resources"
      },
      {
        type: "input",
        name: "searchPath",
        message:
          "What is the search path of your resource? (example: /some-collection will produce /some-collection/search)",
        default: "resources"
      },
      {
        type: "input",
        name: "fileName",
        message:
          "What is the name of your OpenAPI document? (example: message will produce message.opeapi.yaml file)",
        default: "resource"
      },
      {
        type: "input",
        name: "apiName",
        message:
          "What is the title of your API? (example: Business-Participants, will produce Business-Participants JSON:API Interface",
        default: "resource"
      }
    ];

    return this.prompt(prompts).then(props => {
      // To access props later use this.props.someAnswer;
      this.props = props;
    });
  }

  writing() {
    this.log("Writing templates...");

    var destinationProject = "openapi/" + this.props.apiType;
    var isSix = lodash.lowerCase(this.props.isSix);
    var path = "swagger/six/yeoman.openapi.yaml";

    if (isSix === "darwin") {
      path = "swagger/api/yeoman.openapi.yaml";
    }

    this.fs.copyTpl(
      this.templatePath(path),
      this.destinationPath(
        destinationProject + "/" + this.props.fileName + ".openapi.yaml"
      ),
      {
        project: this.props.project,
        apiName: this.props.apiName,
        searchPath: this.props.searchPath,
        resource: this.props.resource,
        resourceUpr: this.props.resource.toUpperCase(),
        resourceCamel: lodash.capitalize(lodash.camelCase(this.props.resource))
      }
    );
  }
};