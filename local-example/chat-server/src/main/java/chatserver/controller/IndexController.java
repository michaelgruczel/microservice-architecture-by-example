/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package chatserver.controller;

import chatserver.config.MessageRepositoryConfig;
import chatserver.model.Message;
import chatserver.model.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

	private MessageRepository repository;

    public IndexController(MessageRepository repository) {
        this.repository = repository;
	}

	@RequestMapping("/")
	public String index(Model model) {

		List<Message> list = new ArrayList<>();
		repository.findAll().forEach(list::add);
		model.addAttribute("messages", list);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		model.addAttribute("message", new Message(currentPrincipalName, ""));
		return "index";
	}

	@RequestMapping(path = "/message", method = RequestMethod.POST)
	public String addMessage(Model model, @ModelAttribute Message message) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		message.setAuthor(currentPrincipalName);
		System.out.println(message.getAuthor());
		repository.save(message);
		return "redirect:/";
	}

	@RequestMapping("/flushdb")
	public String flushdb(Model model) {
		repository.deleteAll();
		return "redirect:/";
	}

}
