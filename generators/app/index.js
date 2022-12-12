"use strict";

const Generator = require("yeoman-generator");

module.exports = class extends Generator {
  prompting() {
	this.log("........................................................");
	this.log("........................................................");
	this.log("                      QUERY GENERATOR - v 0.0.1         ");
	this.log("                                                        ");

    const prompts = [
      {
        type: "input",
        name: "projectName",
        message:
          "What is the name of the project you want to build? (clp-bck-[PROJECT_NAME]-q)",
		validate: input => /^([a-z]{1}[a-z0-9-]*(\.[a-z]{1}[0-9a-z]*)*)$/.test(input) ? true : 'Invalid project name',
        default: "resources"
      },
	  {
        type: "input",
        name: "fileName",
        message:
          "fileName",
		validate: input => /^([a-z]{1}[a-z0-9-]*(\.[a-z]{1}[0-9a-z]*)*)$/.test(input) ? true : 'Invalid project name',
        default: "resources"
      },
      {
        type: "input",
        name: "resource",
        message:
          "What is the RESOURCE that you want to model? (your API should be /[RESOURCE]/{id}. Please, use lowercase!)",
        default: "resources"
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
        name: "apiName",
        message:
          "What is the title of your API? (example: Business-Participants, will produce Business-Participants JSON:API Interface",
        default: "resource"
      },
	  {
        type: "input",
        name: "mongoCollection",
        message:
          "What is the collection in Mongo DB where we will read? (format: DCLEAR.MUR.[KS|DAT|CALC].[COLLECTION]. Example: DCLEAR.MUR.KS.SOMETHING",
        default: "DCLEAR.MUR.KS.SOMETHING"
      }
    ];
	
	return this.prompt(prompts).then(props => {
      // To access props later use this.props.someAnswer;
      this.props = props;
    });
  }

  writing() {
	  //OPENAPI
	  this.log("Writing templates...");

    var destinationProject = "openapi/" + this.props.apiType;
    var path = "swagger/api/yeoman.openapi.yaml";
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
      }
    );

	  //JAVA SPRINGBOOT
	  this.fs.copyTpl(
		this.templatePath("service-code/"),
		  this.destinationPath(this.props.projectName),
		  {
			projectName: this.props.projectName,
			resourceDot: this.props.resource.replace("-","."),
			resource: this.props.resource,
			mongoCollection: this.props.mongoCollection
		  }
		

	  );
	  console.log(this.props);
	  
	  
  }
};
