package com.example.tanzu.branch.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.tanzu.branch.entity.Branch;
import com.example.tanzu.branch.entity.BranchDetail;
import com.example.tanzu.branch.entity.BranchNote;
import com.example.tanzu.branch.model.BranchModel;
import com.example.tanzu.branch.repository.BranchDetailRepository;
import com.example.tanzu.branch.repository.BranchNoteRepository;
import com.example.tanzu.branch.repository.BranchRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@OpenAPIDefinition(
        info = @Info(
                title = "Branch Service",
                version = "0.0.1",
        		description = "Service for maintaining for Branches"),
        tags = @Tag(
                name = "BRANCH REST API",
                description = "Branch Search API"))
@RestController
@RequestMapping("branch")
@Slf4j
public class BranchResource 
{
	protected BranchRepository branchRepo;

	protected BranchNoteRepository branchNoteRepo;
	
	protected BranchDetailRepository branchDetailRepo;
	
	@Autowired
	public void setBranchRepository(BranchRepository branchRepo)
	{
		this.branchRepo = branchRepo;
	}
	
	@Autowired
	public void setBranchNoteRepository(BranchNoteRepository branchNoteRepo)
	{
		this.branchNoteRepo = branchNoteRepo;
	}
	
	@Autowired
	public void setATMDetailRepository(BranchDetailRepository branchDetailRepo)
	{
		this.branchDetailRepo = branchDetailRepo;
	}	
	
	@GetMapping("/{id}")
	public Mono<BranchModel> findById(@PathVariable("id") Long id)
	{
		return branchRepo.findById(id)
		.flatMap(branch ->
		{
			return branchNoteRepo.findByBranchId(id)
					.collectList()
					.switchIfEmpty(Mono.just(Collections.emptyList()))
					.map(notes -> 
					{
						return new BranchModel(branch.id(), branch.name(), branch.addr(), branch.city(), branch.state(), 
								branch.postalCode(), null,  notes);
					})
					.flatMap(branchN ->
					{
						return branchDetailRepo.findByBranchId(id)
							.collectList()
							.switchIfEmpty(Mono.just(Collections.emptyList()))
							.map(details -> 
							{
								return new BranchModel(branch.id(), branch.name(), branch.addr(), branch.city(), branch.state(), 
										branch.postalCode(), details,  branchN.notes());
							});
					});				
		})
 	   .onErrorResume(e -> { 
	    	log.error("Error retrieving branch", e);
	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
	   });

	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<BranchModel> addBranch(@RequestBody BranchModel branch)
	{
		log.info("Adding branch with name {}", branch.name());
	
		final var newBranch = new Branch(branch.id(), branch.name(), branch.addr(), branch.city(), branch.state(), branch.postalCode());
		
		return branchRepo.save(newBranch)
			.flatMap(savedBranch -> 
			{
				Mono<BranchModel> saveDetailMono = null;
				
				if (CollectionUtils.isEmpty(branch.details()))
					saveDetailMono = Mono.just(new BranchModel(savedBranch.id(), savedBranch.name(), savedBranch.addr(), savedBranch.city(), savedBranch.state(), 
							savedBranch.postalCode(), Collections.emptyList(),  null));
				else
				{
					final List<BranchDetail> details = new ArrayList<>();
					for (BranchDetail detail : branch.details())
						details.add(new BranchDetail(null, detail.detail(), savedBranch.id()));
						
					saveDetailMono = branchDetailRepo.saveAll(details)
					  .collectList()
					  .map(savedDetails -> new BranchModel(savedBranch.id(), savedBranch.name(), savedBranch.addr(), savedBranch.city(), savedBranch.state(), 
							  savedBranch.postalCode(), savedDetails,  null));
				}
					
				return saveDetailMono.flatMap(sBranch ->
				{
					if (CollectionUtils.isEmpty(branch.notes()))
						return Mono.just(new BranchModel(savedBranch.id(), sBranch.name(), sBranch.addr(), sBranch.city(), sBranch.state(), 
								sBranch.postalCode(), sBranch.details(),  Collections.emptyList()));
					
					final List<BranchNote> notes = new ArrayList<>();
					for (BranchNote note : branch.notes())
						notes.add(new BranchNote(null, note.note(), savedBranch.id()));
					
					return branchNoteRepo.saveAll(notes).collectList()
						.map(addedNotes -> new BranchModel(savedBranch.id(), sBranch.name(), sBranch.addr(), sBranch.city(), sBranch.state(), 
								sBranch.postalCode(), sBranch.details(),  addedNotes));
				});
				
			})
    	   .onErrorResume(e -> { 
    	    	log.error("Error adding for Branch.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}	
	
	@DeleteMapping("{id}") 
	public Mono<Void> deleteBranch(@PathVariable("id") Long id)
	{
		log.info("Deleting Branch with id {}", id);
		
		return branchRepo.deleteById(id)
			.then(branchDetailRepo.deleteByBranchId(id))
			.then(branchNoteRepo.deleteByBranchId(id))
    	   .onErrorResume(e -> { 
    	    	log.error("Error deleting Branch.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}
}
