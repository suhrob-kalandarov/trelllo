package org.exp.trello.controllers.home;

import org.exp.trello.models.entities.TaskColumn;
import org.exp.trello.repositories.TaskColumnRepository;
import org.exp.trello.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/column")
public class TaskColumnController {

    @Autowired
    private TaskColumnRepository taskColumnRepository;

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/create")
    public String createColumn(@RequestParam String name,
                               @RequestParam(name = "active", defaultValue = "false") boolean active,
                               RedirectAttributes redirectAttributes
    ) {
        // Get the highest position value
        Integer maxPosition = taskColumnRepository.findMaxPosition();
        int newPosition = (maxPosition != null) ? maxPosition + 1 : 1;

        // Create new column
        TaskColumn column = new TaskColumn();
        column.setName(name);
        column.setInActive(!active);
        column.setPosition(newPosition);

        taskColumnRepository.save(column);

        redirectAttributes.addFlashAttribute("successMessage", "Column created successfully");
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteColumn(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        taskColumnRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Column deleted successfully");
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<TaskColumn> optionalColumn = taskColumnRepository.findById(id);

        if (optionalColumn.isPresent()) {
            TaskColumn column = optionalColumn.get();
            Long taskCount = taskRepository.countByColumnId(column.getId());
            model.addAttribute("column", column);
            model.addAttribute("columnTaskCount", taskCount);
            return "column/edit-column";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/update/{id}")
    public String updateColumn(@PathVariable Integer id,
                               @RequestParam String name,
                               @RequestParam(required = false, defaultValue = "false") boolean inActive,
                               RedirectAttributes redirectAttributes) {
        Optional<TaskColumn> columnOpt = taskColumnRepository.findById(id);

        if (columnOpt.isPresent()) {
            TaskColumn column = columnOpt.get();
            column.setName(name);
            column.setInActive(inActive);

            taskColumnRepository.save(column);

            redirectAttributes.addFlashAttribute("successMessage", "Column updated successfully");
        }

        return "redirect:/";
    }
}