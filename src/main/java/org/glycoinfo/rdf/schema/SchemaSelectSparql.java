package org.glycoinfo.rdf.schema;

import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.scint.ClassHandler;

public class SchemaSelectSparql extends SelectSparqlBean {

/*
	Prefix schema: <http://schema.org/>
		Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
		Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
		Select ?domain 
		Where
		{
		  schema:Person rdf:type owl:Class .
		  ?domain rdfs:domain schema:Person .
		}
*/
	SchemaSelectSparql(ClassHandler classHandler) {
		super.prefix = SchemaSparqlFormatter.getPrefixDefinition(classHandler) + "\n";
		super.prefix += "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n";
		super.select = "distinct ?result";
//		if (classHandler.getPrefixIri().contains("<"))
//		  super.from = "FROM <" + classHandler.getPrefixIri().substring(1, classHandler.getPrefixIri().length() - 1)  + ">";
//		else
//		  super.from = "FROM <" + classHandler.getPrefixIri()  + ">";
		super.where = SchemaSparqlFormatter.getCommonClassWhere(classHandler) + " \n";
	}

	/*
	 *
	 * 
Subject	Predicate	Object	Context
schema:Person rdf:type owl:Class
schema:Person rdfs:label "Person"@en
schema:Person rdfs:comment ""@en
schema:Person rdfs:isDefinedBy schema:Person
schema:Person rdfs:subClassOf schema:Thing
schema:accountablePerson rdfs:range schema:Person
_:node19nj14kvmx13 rdf:first schema:Person
schema:actor rdfs:range schema:Person
schema:actors rdfs:range schema:Person
schema:additionalName rdfs:isDefinedBy schema:Person
schema:additionalName rdfs:domain schema:Person
schema:address rdfs:isDefinedByschema:Person
_:node19nj14kvmx28 rdf:first schema:Person
schema:affiliation rdfs:isDefinedBy schema:Person
schema:affiliation rdfs:domain schema:Person
_:node19nj14kvmx38 rdf:firstschema:Person
schema:alumni rdfs:range schema:Person
schema:alumniOf rdfs:isDefinedByschema:Person
schema:alumniOf rdfs:domain schema:Person
_:node19nj14kvmx70 rdf:first schema:Person
_:node19nj14kvmx73 rdf:first schema:Person
_:node19nj14kvmx80 rdf:first schema:Person
schema:award rdfs:isDefinedBy schema:Person
_:node19nj14kvmx109 rdf:first schema:Person
schema:awards rdfs:isDefinedBy schema:Person
_:node19nj14kvmx112 rdf:first schema:Person
schema:borrower rdfs:range schema:Person
schema:brand rdfs:isDefinedBy schema:Person
_:node19nj14kvmx121 rdf:first schema:Person
schema:buyer rdfs:range schema:Person
schema:candidate rdfs:range schema:Person
schema:children rdfs:isDefinedBy schema:Person
schema:children rdfs:range schema:Person
schema:children rdfs:domain schema:Person
schema:colleague rdfs:isDefinedBy schema:Person
schema:colleague rdfs:range schema:Person
schema:colleague rdfs:domain schema:Person
schema:colleagues rdfs:isDefinedBy schema:Person
schema:colleagues rdfs:range schema:Person
schema:colleagues rdfs:domain schema:Person
schema:contactPoint rdfs:isDefinedBy schema:Person
_:node19nj14kvmx154 rdf:first schema:Person
schema:contactPoints rdfs:isDefinedBy schema:Person
_:node19nj14kvmx157 rdf:first schema:Person
_:node19nj14kvmx163 rdf:first schema:Person
_:node19nj14kvmx166 rdf:first chema:Person
_:node19nj14kvmx175 rdf:first schema:Person
_:node19nj14kvmx178 rdf:first schema:Person
schema:director rdfs:range schema:Person
schema:directors rdfs:range schema:Person
schema:duns rdfs:isDefinedBy schema:Person
_:node19nj14kvmx208 rdf:first schema:Person
schema:editor rdfs:range schema:Person
schema:email rdfs:isDefinedBy schema:Person
_:node19nj14kvmx244 rdf:first schema:Person
schema:employee rdfs:range schema:Person
schema:employees rdfs:range schema:Person
_:node19nj14kvmx247 rdf:first schema:Person
_:node19nj14kvmx250 rdf:first schema:Person
schema:familyName rdfs:isDefinedBy chema:Person
schema:familyName  rdfs:domain schema:Person
schema:faxNumber rdfs:isDefinedBy schema:Person
_:node19nj14kvmx277  rdf:firstschema:Person
_:node19nj14kvmx284 rdf:first schema:Person
schema:follows  rdfs:isDefinedBy schema:Person
schema:follows rdfs:range schema:Person
schema:follows rdfs:domain schema:Person
schema:founder  rdfs:range schema:Person
schema:founders rdfs:range schema:Person
schema:gender rdfs:isDefinedBy schema:Person
schema:gender rdfs:domain schema:Person
schema:givenName rdfs:isDefinedBy schema:Person
schema:givenName rdfs:domain schema:Person
schema:globalLocationNumber rdfs:isDefinedBy schema:Person
_:node19nj14kvmx300 rdf:first schema:Person
schema:hasPOS rdfs:isDefinedBy schema:Person
_:node19nj14kvmx319 rdf:first schema:Person
schema:homeLocation rdfs:isDefinedBy schema:Person
schema:homeLocation rdfs:domain schema:Person
schema:honorificPrefix rdfs:isDefinedBy schema:Person
schema:honorificPrefix rdfs:domain schema:Person
schema:honorificSuffix rdfs:isDefinedBy schema:PersonAnneImberty 
schema:honorificSuffix rdfs:domain schema:Person
schema:illustrator rdfs:range schema:Person
schema:interactionCount rdfs:isDefinedBy schema:Person
_:node19nj14kvmx341 rdf:first schema:Person
schema:isicV4 rdfs:isDefinedBy schema:Person
_:node19nj14kvmx349 rdf:first schema:Person
schema:jobTitle rdfs:isDefinedBy schema:Person
schema:jobTitle rdfs:domain schema:Person
schema:knows rdfs:isDefinedBy schema:Person
schema:knows  rdfs:range schema:Person
schema:knows rdfs:domain schema:Person
_:node19nj14kvmx363 rdf:first schema:Person
schema:lender  rdfs:range schema:Person
schema:loser rdfs:range  schema:Person
schema:makesOffer rdfs:isDefinedBy schema:Person
_:node19nj14kvmx396rdf:first  schema:Person
_:node19nj14kvmx410 rdf:first chema:Person
schema:memberOf rdfs:isDefinedBy schema:Person

	 *
	 * 
insert into oauth_client_token (token_id, token, authentication_id, user_name, client_id) values (?, ?, ?, ?, ?)";

token_id - uri hash
token - token string ? 
authentication_id - keyGenerator.extractKey(resource, authentication) // a unique key identifying an access token for this pair
user_name - Person.email

client_id - oauth client id  - why save this? no need

schema:Person is the variable:

Prefix schema: <http://schema.org/>
Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
Select ?domain 
Where
{
  schema:Person rdf:type owl:Class .
  ?domain rdfs:domain schema:Person .
}
	 *
	 * 
	 * 

PREFIX schema: <http://schema.org/>
Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
Select ?domain ?parentClass
Where
{
#  schema:Person rdf:type owl:Class .
#  ?range rdfs:range schema:Person .
  schema:Person rdfs:subClassOf ?parentClass .
  ?domain rdfs:domain ?parentClass .
}

PREFIX schema: <http://schema.org/>
Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
Prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
Select ?range ?parentClass
Where
{
  schema:Person rdf:type owl:Class .
  schema:Person rdfs:subClassOf ?parentClass .
  ?range rdfs:range ?parentClass .
}

	 */
	
	
}